package mk.ukim.finki.wayzi.service.domain.impl;


import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.model.exception.ProfilePictureNotFoundException;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
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
    private final StandardUserRepository standardUserRepository;

    public UserServiceImpl(AuthService authService, StandardUserRepository standardUserRepository) {
        this.authService = authService;
        this.standardUserRepository = standardUserRepository;
    }

    @Override
    public void submitProfilePic(MultipartFile profilePic) {
        StandardUser user = authService.getAuthenticatedStandardUser();

        try {
            byte[] imageBytes = profilePic.getBytes();
            user.setProfilePic(imageBytes);
            standardUserRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }

    }

    @Override
    public Resource loadProfilePicAsResource(Long userId) {
        StandardUser user = standardUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("error"));

        byte[] imageBytes = user.getProfilePic();
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ProfilePictureNotFoundException(userId);
        }

        return new ByteArrayResource(imageBytes);
    }
}
