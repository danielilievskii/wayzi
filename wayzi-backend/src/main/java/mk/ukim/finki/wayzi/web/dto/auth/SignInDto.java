package mk.ukim.finki.wayzi.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record SignInDto(
        @NotEmpty(message = "Email is required")
        @Email
        String email,

        @NotEmpty(message = "Password is required")
        String password
) {}
