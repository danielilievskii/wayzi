package mk.ukim.finki.wayzi.converter;

import mk.ukim.finki.wayzi.dto.SignUpDto;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public UserConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public StandardUser toEntity(SignUpDto signUpDto) {
        return new StandardUser(
                signUpDto.email(),
                passwordEncoder.encode(signUpDto.password()),
                signUpDto.name()
        );
    }
}
