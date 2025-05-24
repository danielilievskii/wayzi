package mk.ukim.finki.wayzi.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import mk.ukim.finki.wayzi.web.dto.auth.AuthUserDto;
import mk.ukim.finki.wayzi.web.dto.auth.SignInDto;
import mk.ukim.finki.wayzi.web.dto.auth.SignUpDto;
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
    public ResponseEntity<AuthUserDto> signUp(@Valid @RequestBody SignUpDto signUpDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(authApplicationService.signUp(signUpDto, request, response));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthUserDto> signIn(@Valid @RequestBody SignInDto signInDto,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        return ResponseEntity.ok(authApplicationService.signIn(signInDto, request, response));
    }

    @PostMapping("/signout")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        authApplicationService.signOut(response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam("token") String token) {
        authApplicationService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserDto> getAuthenticatedUser() {
        return ResponseEntity.ok(authApplicationService.getAuthenticatedUser());
    }
}

