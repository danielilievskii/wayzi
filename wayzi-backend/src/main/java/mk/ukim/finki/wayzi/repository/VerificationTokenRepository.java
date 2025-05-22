package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    Optional<VerificationToken> findByUserId(Long id);

    @Modifying
    @Query("DELETE FROM VerificationToken t WHERE t.expiryDate < ?1 OR t.used = true")
    void deleteAllExpiredOrUsedSince(LocalDateTime dateTime);

    List<VerificationToken> findByExpiryDateBeforeAndUsedFalse(LocalDateTime dateTime);
}
