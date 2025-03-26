package mk.ukim.finki.wayzi.service.impl;

import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import mk.ukim.finki.wayzi.repository.StandardUserRepository;
import mk.ukim.finki.wayzi.service.AuthService;
import mk.ukim.finki.wayzi.service.StandardUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class StandardUserServiceImpl implements StandardUserService {

    private final StandardUserRepository standardUserRepository;
    private final AuthService authService;

    private final Path profilePicStorageLocation;

    public StandardUserServiceImpl(@Value("./uploads") String uploadDir, StandardUserRepository standardUserRepository, AuthService authService) {
        this.standardUserRepository = standardUserRepository;
        this.authService = authService;

        this.profilePicStorageLocation = Paths.get(uploadDir + "/profile-pics").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.profilePicStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    @Override
    public Optional<StandardUser> findById(Long id) {
        return standardUserRepository.findById(id);
    }

    @Override
    public Boolean uploadProfilePic(MultipartFile file) {
        StandardUser standardUser = authService.getAuthenticatedStandardUser();

        Path passengerProfilePicDir = this.profilePicStorageLocation.resolve(String.valueOf(standardUser.getId()));

        try {
            Files.createDirectories(passengerProfilePicDir);
//            String originalFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String originalFileName = file.getOriginalFilename();

            if(originalFileName != null) {
                Path targetLocation = passengerProfilePicDir.resolve(originalFileName);

                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                String relativePath = Paths.get("uploads", "profile-pics", String.valueOf(standardUser.getId()), originalFileName).toString();
                standardUser.setProfilePicPath(relativePath);
                standardUserRepository.save(standardUser);
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Please try again!", e);
        }

        return true;
    }
}
