package mk.ukim.finki.wayzi.service.domain.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.VerificationToken;
import mk.ukim.finki.wayzi.model.domain.User;
import mk.ukim.finki.wayzi.model.exception.ConflictException;
import mk.ukim.finki.wayzi.model.exception.ResourceNotFoundException;
import mk.ukim.finki.wayzi.repository.UserRepository;
import mk.ukim.finki.wayzi.repository.VerificationTokenRepository;
import mk.ukim.finki.wayzi.service.domain.NotificationService;
import mk.ukim.finki.wayzi.service.domain.VerificationTokenService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository tokenRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public VerificationToken createVerificationToken(User user) {
        VerificationToken token = new VerificationToken();

        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        token.setUsed(false);

        VerificationToken savedToken = tokenRepository.save(token);
        notificationService.sendEmailVerification(token);

        return savedToken;
    }

    @Override
    @Transactional
    public VerificationToken validateToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("The verification link is invalid."));

        if (verificationToken.isExpired()) {
            throw new IllegalStateException("This verification link has expired.");
        }

        if (verificationToken.isUsed()) {
            throw new ConflictException("Email already verified.");
        }

        User user = verificationToken.getUser();
        user.setIsEmailVerified(true);
        userRepository.save(user);

        verificationToken.setUsed(true);
        tokenRepository.save(verificationToken);

        notificationService.notifyUserEmailVerified(user);

        return verificationToken;
    }

    @Override
    @Scheduled(cron = "0 0 */1 * * *")
    @Transactional
    public void deleteExpiredTokens() {
        List<VerificationToken> expiredTokens = tokenRepository
                .findByExpiryDateBeforeAndUsedFalse(LocalDateTime.now());

        expiredTokens.stream()
                .map(VerificationToken::getUser)
                .filter(user -> !user.getIsEmailVerified())
                .forEach(userRepository::delete);

        tokenRepository.deleteAllExpiredOrUsedSince(LocalDateTime.now());
    }

    @Override
    public void deleteToken(VerificationToken token) {
        tokenRepository.delete(token);
    }
}
