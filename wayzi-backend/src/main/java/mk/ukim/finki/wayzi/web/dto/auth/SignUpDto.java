package mk.ukim.finki.wayzi.web.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
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

    @AssertTrue(message = "Passwords must match")
    public boolean isPasswordMatch() {
        return password != null && password.equals(confirmPassword);
    }

}
