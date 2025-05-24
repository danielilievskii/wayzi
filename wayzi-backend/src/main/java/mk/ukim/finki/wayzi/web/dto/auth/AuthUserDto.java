package mk.ukim.finki.wayzi.web.dto.auth;

import mk.ukim.finki.wayzi.model.domain.User;

public record AuthUserDto(
        Long id,
        String email,
        String name,
        String role,
        boolean isEnabled
) {

    public static AuthUserDto from (User user) {
        return new AuthUserDto (
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().getAuthority(),
                user.isEnabled()
        );
    }
}
