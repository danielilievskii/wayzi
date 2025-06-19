package mk.ukim.finki.wayzi.service.domain.impl;

import mk.ukim.finki.wayzi.model.domain.Ride;
import mk.ukim.finki.wayzi.model.enumeration.RideStatus;
import mk.ukim.finki.wayzi.repository.RideRepository;
import mk.ukim.finki.wayzi.service.domain.RideStatusScheduler;
import mk.ukim.finki.wayzi.service.domain.RideStatusService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RideStatusSchedulerImpl implements RideStatusScheduler {

    private final RideStatusService rideStatusService;
    private final RideRepository rideRepository;

    public RideStatusSchedulerImpl(RideStatusService rideStatusService, RideRepository rideRepository) {
        this.rideStatusService = rideStatusService;
        this.rideRepository = rideRepository;
    }

    @Override
    @Scheduled(cron = "0 0,30 * * * *")
    @Transactional
    public void checkAndUpdateStaleRides() {
        LocalDateTime now = LocalDateTime.now();

        // Auto-cancel PENDING rides that should have started
        List<Ride> stalePending = rideRepository.findByStatusAndDepartureTimeBefore(RideStatus.PENDING, now);
        for (Ride ride : stalePending) {
            rideStatusService.transitionTo(ride.getId(), RideStatus.CANCELLED);
        }

        // Auto-cancel CONFIRMED rides that missed their departure for 30 minutes
        List<Ride> missedConfirmed = rideRepository.findByStatusAndDepartureTimeBefore(RideStatus.CONFIRMED, now.plusMinutes(30));
        for (Ride ride : missedConfirmed) {
            rideStatusService.transitionTo(ride.getId(), RideStatus.CANCELLED);
        }

        // Auto-finish STARTED rides that should have finished before 3 hours
        List<Ride> longStarted = rideRepository.findByStatusAndArrivalTimeBefore(RideStatus.STARTED, now.plusHours(3));
        for (Ride ride : longStarted) {
            rideStatusService.transitionTo(ride.getId(), RideStatus.FINISHED);
        }
    }
}
