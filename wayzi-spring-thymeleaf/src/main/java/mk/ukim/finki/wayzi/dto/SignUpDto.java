package mk.ukim.finki.wayzi.dto;

import jakarta.validation.constraints.*;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpDto(
        @NotEmpty(message = "Please enter your name")
        String name,

        @NotEmpty(message = "Please enter your email")
        @Email
        String email,

        //    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).{8,}$", message = "Password must contain at least one uppercase letter, one number, and one special character")
        @Size(min = 5, message = "Password must be at least 5 characters")
        String password,

        @NotEmpty(message = "Please confirm your password")
        String confirmPassword

) {

    public SignUpDto() {
        this(null, "", "", "");
    }

    public StandardUser toEntity(PasswordEncoder passwordEncoder) {
        return new StandardUser(
                email,
                passwordEncoder.encode(password),
                name
        );
    }

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }
}





