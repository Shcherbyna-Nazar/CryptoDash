package com.example.cryptoapp.auth.service;

import com.example.cryptoapp.auth.other.AuthenticationRequest;
import com.example.cryptoapp.auth.other.AuthenticationResponse;
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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(ReqisterRequest request) {
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
}
