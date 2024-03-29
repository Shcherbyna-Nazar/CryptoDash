package com.example.cryptoapp.auth.controller;

import com.example.cryptoapp.auth.other.AuthenticationRequest;
import com.example.cryptoapp.auth.other.AuthenticationResponse;
import com.example.cryptoapp.auth.other.GoogleTokenVerificationRequest;
import com.example.cryptoapp.auth.service.AuthenticationService;
import com.example.cryptoapp.auth.other.ReqisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody ReqisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
    @PostMapping("/google")
    public ResponseEntity<AuthenticationResponse> authenticateWithGoogle(@RequestBody GoogleTokenVerificationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticateWithGoogle(request));
    }
}
