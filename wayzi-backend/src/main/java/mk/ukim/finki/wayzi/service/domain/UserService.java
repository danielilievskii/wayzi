package mk.ukim.finki.wayzi.service.domain;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void submitProfilePic(MultipartFile profilePic);
    Resource loadProfilePicAsResource(Long userId);
}
