package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.VerificationToken;
import mk.ukim.finki.wayzi.model.domain.user.User;

public interface VerificationTokenService {
    VerificationToken createVerificationToken(User user);
    VerificationToken validateToken(String token);
    void deleteExpiredTokens();
    void deleteToken(VerificationToken token);
}
