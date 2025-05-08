package mk.ukim.finki.wayzi.service.application.impl;

import mk.ukim.finki.wayzi.service.application.UserApplicaitonService;
import mk.ukim.finki.wayzi.service.domain.UserService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserApplicationServiceImpl implements UserApplicaitonService {

    private final UserService userService;

    public UserApplicationServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void submitProfilePic(MultipartFile profilePic) {
        userService.submitProfilePic(profilePic);
    }

    @Override
    public Resource loadProfilePicAsResource(Long userId) {
       return userService.loadProfilePicAsResource(userId);
    }
}
