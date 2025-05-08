package mk.ukim.finki.wayzi.service.application;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserApplicaitonService {
    void submitProfilePic(MultipartFile profilePic);
    Resource loadProfilePicAsResource(Long userId);
}
