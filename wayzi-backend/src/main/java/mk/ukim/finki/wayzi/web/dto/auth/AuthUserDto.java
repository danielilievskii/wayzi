package mk.ukim.finki.wayzi.web.dto.auth;

import mk.ukim.finki.wayzi.model.domain.user.User;

public record AuthUserDto(Long id, String email, String name, String role) {

    public static AuthUserDto from (User user) {
        return new AuthUserDto (
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getAuthority()
        );
    }
}
