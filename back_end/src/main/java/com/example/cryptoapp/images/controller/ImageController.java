package com.example.cryptoapp.images.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("api/v1/images")
public class ImageController {

    private final Path rootLocation = Paths.get("user_photos");

    @GetMapping("/{userEmail}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String userEmail, @PathVariable String filename) {
        try {
            // Resolve the path to include the user-specific directory
            Path userDirectory = rootLocation.resolve(userEmail);
            Path file = userDirectory.resolve(filename);

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

}
