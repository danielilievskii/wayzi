package mk.ukim.finki.wayzi.service.domain.impl;


import jakarta.transaction.Transactional;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.exception.ProfilePictureNotFoundException;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.service.domain.AuthService;
import mk.ukim.finki.wayzi.service.domain.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    private final AuthService authService;
    private final UserRepository userRepository;

    public UserServiceImpl(AuthService authService,  UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Override
    public void submitProfilePic(MultipartFile profilePic) {
        User user = authService.getAuthenticatedUser();

        try {
            byte[] imageBytes = profilePic.getBytes();
            user.setProfilePic(imageBytes);
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }

    }

    @Override
    @Transactional
    public Resource loadProfilePicAsResource(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("error"));

        byte[] imageBytes = user.getProfilePic();
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ProfilePictureNotFoundException(userId);
        }

        return new ByteArrayResource(imageBytes);
    }
}
