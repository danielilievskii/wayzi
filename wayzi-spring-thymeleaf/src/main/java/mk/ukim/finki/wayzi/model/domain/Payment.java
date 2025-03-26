package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.enumeration.PaymentStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    private String transactionId;

    private LocalDateTime paymentDate;

    @OneToOne(mappedBy = "payment")
    private RideBooking rideBooking;

    public Payment(PaymentStatus status, String transactionId, RideBooking rideBooking) {
        this.status = status;
        this.transactionId = transactionId;
        this.paymentDate = LocalDateTime.now();
        this.rideBooking = rideBooking;
    }
}
