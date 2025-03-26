package mk.ukim.finki.wayzi.service;

import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface StandardUserService {
    Optional<StandardUser> findById(Long id);

    Boolean uploadProfilePic(MultipartFile file);
}
