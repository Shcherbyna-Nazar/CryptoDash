package com.example.cryptoapp.user.service;

import com.example.cryptoapp.user.model.User;
import com.example.cryptoapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class UserService {

    private final Path rootLocation = Paths.get("user_photos");

    @Autowired
    private UserRepository userRepository;

    public UserService() {
        init();
    }

    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail).orElse(null);
    }

    public User updateUserProfile(String userEmail, String firstName, String lastName, String bio) {
        User user = getUserByEmail(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setBio(bio);
        userRepository.save(user);
        return user;
    }


    public String saveProfilePhoto(String userEmail, MultipartFile file) {
        User user = getUserByEmail(userEmail);
        if (user == null) {
            throw new StorageException("User not found");
        }

        if (file.isEmpty()) {
            throw new StorageException("Cannot store empty file");
        }

        try {
            if (!isImage(file)) {
                throw new StorageException("You can only upload image files");
            }

            Path userDirectory = rootLocation.resolve(userEmail);
            Files.createDirectories(userDirectory);

            String filename = "profile_photo" + getFileExtension(file.getOriginalFilename());  // Это заменит существующий файл
            Path destinationFile = userDirectory.resolve(Paths.get(filename)).normalize().toAbsolutePath();

            if (!destinationFile.getParent().equals(userDirectory.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }

            // Сохраняем файл
            try (var inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                String photoUrl = convertPathToUrl(destinationFile);
                user.setProfilePhotoUrl(photoUrl);
                userRepository.save(user);
                return photoUrl;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }


    private boolean isImage(MultipartFile file) {
        String mimeType = file.getContentType();
        return mimeType != null && mimeType.startsWith("image");
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if(dotIndex >= 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    private String convertPathToUrl(Path path) {
        String filename = path.getFileName().toString();
        return "http://localhost:8080/api/v1/images/" + path.getParent().getFileName() + "/" + filename;
    }


    private void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    public static class StorageException extends RuntimeException {
        public StorageException(String message) {
            super(message);
        }

        public StorageException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
