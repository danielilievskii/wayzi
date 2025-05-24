package mk.ukim.finki.wayzi.service.domain;

import mk.ukim.finki.wayzi.model.domain.VerificationToken;
import mk.ukim.finki.wayzi.model.domain.User;

public interface VerificationTokenService {
    VerificationToken createVerificationToken(User user);
    VerificationToken validateToken(String token);
    void deleteExpiredTokens();
    void deleteToken(VerificationToken token);
}
