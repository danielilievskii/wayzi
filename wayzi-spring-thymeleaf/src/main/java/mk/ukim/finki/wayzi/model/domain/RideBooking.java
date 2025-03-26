package mk.ukim.finki.wayzi.model.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mk.ukim.finki.wayzi.enumeration.PaymentMethod;
import mk.ukim.finki.wayzi.enumeration.RideBookingStatus;
import mk.ukim.finki.wayzi.model.domain.ride.Ride;
import mk.ukim.finki.wayzi.model.domain.user.StandardUser;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class RideBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(value = EnumType.STRING)
    private RideBookingStatus bookingStatus;

    private int bookedSeats;

    private String qrCodeUrl;

    private LocalDateTime bookingTime;

    private boolean driverAbscenseReport;

    private boolean riderAbscenseReport;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private StandardUser rider;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private Ride ride;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    public RideBooking (PaymentMethod paymentMethod, RideBookingStatus bookingStatus, int bookedSeats, String qrCodeUrl, StandardUser rider, Ride ride, Payment payment) {
        this.paymentMethod = paymentMethod;
        this.bookingStatus = bookingStatus;
        this.bookedSeats = bookedSeats;
        this.qrCodeUrl = qrCodeUrl;
        this.bookingTime = LocalDateTime.now();
        this.rider = rider;
        this.ride = ride;
        this.payment = payment;
    }


    public int getTotalPrice() {
        return bookedSeats * ride.getPricePerSeat();
    }
}
