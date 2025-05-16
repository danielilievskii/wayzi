package mk.ukim.finki.wayzi.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.wayzi.model.domain.user.AdminUser;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignUpDto(
        @NotEmpty(message = "Name is required")
        String name,

        @NotEmpty(message = "Email is required")
        @Email
        String email,

        @Size(min = 5, message = "Password must be at least 5 characters")
        String password,

        @NotEmpty(message = "Please confirm your password")
        String confirmPassword

) {

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
