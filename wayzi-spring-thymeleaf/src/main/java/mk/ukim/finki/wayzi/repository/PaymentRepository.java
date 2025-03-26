package mk.ukim.finki.wayzi.repository;

import mk.ukim.finki.wayzi.model.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
