package mk.ukim.finki.wayzi.model.domain.ride;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.model.domain.user.User;
import mk.ukim.finki.wayzi.model.enumeration.CheckInStatus;
import mk.ukim.finki.wayzi.model.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.model.enumeration.RideBookingStatus;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class RideBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "booker_id")
    private User booker;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideBookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    private CheckInStatus checkInStatus;

    private Integer bookedSeats;

    private Integer totalPrice;

    private String message;

    @Column(name = "qr_code_url", length = 1000)
    private String qrCodeUrl;

    private LocalDateTime bookingTime;

    private boolean driverAbsenceReport;

    private boolean riderAbsenceReport;

    public RideBooking(
            Ride ride,
            User booker,
            PaymentMethod paymentMethod,
            RideBookingStatus bookingStatus,
            CheckInStatus checkInStatus,
            Integer bookedSeats,
            Integer totalPrice,
            String qrCodeUrl,
            LocalDateTime bookingTime,
            boolean driverAbsenceReport,
            boolean riderAbsenceReport
    ) {
        this.ride = ride;
        this.booker = booker;
        this.paymentMethod = paymentMethod;
        this.bookingStatus = bookingStatus;
        this.checkInStatus = checkInStatus;
        this.bookedSeats = bookedSeats;
        this.totalPrice = totalPrice;
        this.qrCodeUrl = qrCodeUrl;
        this.bookingTime = bookingTime;
        this.driverAbsenceReport = driverAbsenceReport;
        this.riderAbsenceReport = riderAbsenceReport;
    }
}
