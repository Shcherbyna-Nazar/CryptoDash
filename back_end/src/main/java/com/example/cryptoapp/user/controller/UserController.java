package com.example.cryptoapp.user.controller;

import com.example.cryptoapp.user.model.User;
import com.example.cryptoapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{userEmail}")
    public ResponseEntity<User> getUserProfile(@PathVariable String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping("/{userEmail}/uploadPhoto")
    public ResponseEntity<?> uploadProfilePhoto(@PathVariable String userEmail, @RequestParam("file") MultipartFile file) {
        try {
            String photoUrl = userService.saveProfilePhoto(userEmail, file);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Photo uploaded successfully");
            response.put("photoUrl", photoUrl);
            return ResponseEntity.ok().body(response);  // Return a JSON response
        } catch (UserService.StorageException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file", e);
        }
    }


}
