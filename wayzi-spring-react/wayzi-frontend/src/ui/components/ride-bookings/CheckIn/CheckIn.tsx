import defaultProfilePic from "../../../../../public/assets/images/default-profile-pic.png";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../../../../redux/store.ts";
import {checkInPassenger} from "../../../../redux/slices/rideBookingCheckInSlice.ts";

export const CheckIn = (props) => {

    const {rideBooking, errorCheckIn, loadingCheckIn} = props
    const dispatch = useDispatch<AppDispatch>()

    const handleCheckIn = () => {
        dispatch(checkInPassenger(rideBooking.rideBookingId))
    }




    return (
        <div className="row justify-content-center">
            <div className="col-lg-8 col-md-10">
                <div className="card shadow-sm rounded-4 border-0">
                    <div className="card-body p-5">

                        {/* Header */}
                        <div className="d-flex justify-content-between align-items-center mb-4">
                            <h3 className="fw-bold text-dark mb-0">Passenger Check-In</h3>
                            <span className="badge bg-light text-dark border fw-semibold">
                                Booking No: {rideBooking.rideBookingId}
                            </span>
                        </div>

                        {/* Booker Info */}
                        <div className="d-flex align-items-center gap-3 mb-4">
                            <img
                                src={defaultProfilePic}
                                alt="Passenger"
                                className="rounded-circle"
                                width="60"
                                height="60"
                            />
                            <div>
                                <h5 className="fw-semibold mb-0">{rideBooking.bookerName}</h5>
                                <div className="text-muted small">🎟️ {rideBooking.bookedSeats} seat(s) booked</div>
                            </div>
                        </div>

                        <hr />

                        {/* Booking Statuses */}
                        <div className="row g-4 justify-content-center">
                            <div className="col-md-6">
                                <div className="text-muted small mb-1">Booking Status</div>
                                <span className={`badge fw-medium ${
                                    rideBooking.rideBookingStatus === "CONFIRMED" ? "bg-info-subtle text-info" :
                                        rideBooking.rideBookingStatus === "CANCELLED" ? "bg-danger-subtle text-danger" :
                                            "bg-warning-subtle text-warning"
                                }`}>
      {rideBooking.rideBookingStatus.replace(/_/g, " ")}
    </span>
                            </div>
                            <div className="col-md-6">
                                <div className="text-muted small mb-1">Check-In Status</div>
                                <span className={`badge fw-medium ${
                                    rideBooking.checkInStatus === "CHECKED_IN"
                                        ? "bg-success-subtle text-info"
                                        : "bg-warning-subtle text-warning"
                                }`}>
      {rideBooking.checkInStatus.replace(/_/g, " ")}
    </span>
                            </div>
                        </div>


                        {/* Check-In Button */}
                        <div className="mt-5 d-flex justify-content-end">
                            <button
                                className="btn custom-btn px-4 py-2 rounded-3"
                                disabled={rideBooking.checkInStatus === "CHECKED_IN" || loadingCheckIn}
                                onClick={handleCheckIn}
                            >
                                {!loadingCheckIn ? "Check In Passenger" : "Checking In..."}
                            </button>
                        </div>


                    </div>
                </div>
            </div>
        </div>


    )
}