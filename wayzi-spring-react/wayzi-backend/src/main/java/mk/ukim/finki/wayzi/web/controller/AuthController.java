package mk.ukim.finki.wayzi.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.service.application.AuthApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthApplicationService authApplicationService;

    public AuthController(AuthApplicationService authApplicationService) {
        this.authApplicationService = authApplicationService;
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(authApplicationService.signUp(signUpDto, request, response));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto signInDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(authApplicationService.signIn(signInDto, request, response));
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        authApplicationService.signOut(response);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedUser() {
        return ResponseEntity.ok(authApplicationService.getAuthenticatedUser());
    }


}

