package mk.ukim.finki.wayzi.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_STANDARD_USER, ROLE_ADMIN_USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
