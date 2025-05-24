package mk.ukim.finki.wayzi.web.controller;

import mk.ukim.finki.wayzi.service.application.UserApplicaitonService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserApplicaitonService userApplicaitonService;

    public UserController(UserApplicaitonService userApplicaitonService) {
        this.userApplicaitonService = userApplicaitonService;
    }

    @PostMapping("/submit-profile-pic")
    public ResponseEntity<Void> submitProfilePic(
            @RequestParam("file") MultipartFile profilePic) {

        userApplicaitonService.submitProfilePic(profilePic);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/download-profile-pic")
    public ResponseEntity<Resource> downloadProfilePic(@PathVariable("id") Long id) {
        Resource resource = userApplicaitonService.loadProfilePicAsResource(id);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


}

