package com.example.cryptoapp.auth.service;

import com.example.cryptoapp.auth.other.AuthenticationRequest;
import com.example.cryptoapp.auth.other.AuthenticationResponse;
import com.example.cryptoapp.auth.other.GoogleTokenVerificationRequest;
import com.example.cryptoapp.auth.other.ReqisterRequest;
import com.example.cryptoapp.security.JwtUtil;
import com.example.cryptoapp.user.model.Role;
import com.example.cryptoapp.user.model.User;
import com.example.cryptoapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    private static final String CLIENT_ID = "696132791550-ral8gkn00orccirt8g5ar2mp66ul4ovk.apps.googleusercontent.com";

    public AuthenticationResponse register(ReqisterRequest request) {
        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        // Create new user if not exists
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtUtil.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateWithGoogle(GoogleTokenVerificationRequest request) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken != null) {
                Payload payload = idToken.getPayload();
                String userId = payload.getSubject();
                String email = payload.getEmail();
                String firstName = (String) payload.get("given_name");
                String lastName = (String) payload.get("family_name");
                boolean emailVerified = payload.getEmailVerified();

                if (emailVerified) {
                    User user = userRepository.findByEmail(email)
                            .orElseGet(() -> {
                                // Если пользователя нет в базе, регистрируем нового
                                return userRepository.save(User.builder()
                                        .firstName(firstName)
                                        .lastName(lastName)
                                        .email(email)
                                        .role(Role.USER)
                                        .build());
                            });

                    var jwtToken = jwtUtil.generateToken(user);
                    return AuthenticationResponse.builder()
                            .token(jwtToken)
                            .build();
                } else {
                    throw new RuntimeException("Email not verified with Google");
                }
            } else {
                throw new RuntimeException("Invalid Google ID token");
            }
        } catch (Exception e) {
            throw new RuntimeException("Google authentication failed", e);
        }
    }
}
