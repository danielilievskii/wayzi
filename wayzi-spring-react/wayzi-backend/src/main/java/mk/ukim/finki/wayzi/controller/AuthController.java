package mk.ukim.finki.wayzi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import mk.ukim.finki.wayzi.dto.SignInDto;
import mk.ukim.finki.wayzi.dto.AuthUserDto;
import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpDto signUpDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(authService.signUp(signUpDto, request, response));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInDto signInDto,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        return ResponseEntity.ok(authService.signIn(signInDto, request, response));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(HttpServletResponse response) {
        authService.signOut(response);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        AuthUserDto authUserDto = authService.getCurrentUser(request);
        return ResponseEntity.ok(authUserDto);
    }


}

