import {useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {formatDateTime} from "../../../utils/dateUtils.ts";
import {downloadProfilePic} from "../../../redux/slices/profilePicSlice.ts";
import defaultProfilePic from "../../../../public/assets/images/default-profile-pic.png"
import {fetchBookersByRideId} from "../../../redux/slices/rideBookersSlice.ts";
import {Link} from "react-router-dom";

export const RideBookersPage = () => {

    const dispatch = useDispatch<AppDispatch>();

    const {rideId} = useParams();

    const rideBookersState = useSelector((state: RootState) => state.rideBookers);
    const {ride} = rideBookersState

    const {pictures} = useSelector((state: RootState) => state.profilePics);
    const [bookerProfilePics, setBookerProfilePics] = useState<Record<string, string>>({});

    useEffect(() => {
        if (!ride && rideId) {
            console.log(rideId)
            dispatch(fetchBookersByRideId(rideId))
        }

    }, [ride, dispatch]);

    useEffect(() => {
        if (ride) {
            const missingPicUserIds = new Set<string>();

            ride.bookers.forEach((user) => {
                if (user.bookerId != null) {
                    const bookerIdStr = String(user.bookerId);
                    if (!pictures[bookerIdStr]) {
                        missingPicUserIds.add(bookerIdStr);
                    }
                }
            });

            missingPicUserIds.forEach((userId) => {
                dispatch(downloadProfilePic(userId));
            });
        }

    }, [ride, dispatch]);

    useEffect(() => {
        if (!ride) return;

        const updatedPics: Record<string, string> = {};

        ride.bookers.forEach(user => {
            const id = String(user.bookerId);
            updatedPics[id] = pictures[id];
        });

        setBookerProfilePics(updatedPics);
    }, [pictures, ride]);


    return (
        <>
            {ride && (
                <div className="page-wrapper">
                    <div className="container py-5">
                        <div className="row justify-content-center">

                            <div className="col-lg-10 col-md-11">

                                <div className="card shadow-sm rounded-4 border-0">
                                    <div className="card-body p-5">
                                        {/* Header */}
                                        <div className="d-flex mb-4">
                                            <div className="d-flex gap-3 align-items-center">
                                                <h4 className="fw-bold text-dark mb-1">
                                                    Bookings
                                                </h4>

                                                <Link to={`/rides/${ride.id}`}>
                                                    <button className="btn btn-light">
                                                        {ride.departureLocation.name} ➜ {ride.arrivalLocation.name}
                                                    </button>
                                                </Link>

                                            </div>
                                        </div>
                                        {/* Passengers */}
                                        {ride.bookers && ride.bookers.length > 0 && (
                                            <div className="mt-5">
                                                <div className="row g-4">
                                                    {ride.bookers.map((booking, index) => (
                                                        <div key={index} className="col-12">
                                                            <div className="rounded-4 py-4 px-5 bg-white shadow">
                                                                <div
                                                                    className="d-flex align-items-center justify-content-between mb-3">
                                                                    <Link
                                                                        key={index}
                                                                        to={`/profile`}
                                                                        className="btn btn-light border-0 text-decoration-none col-12 d-flex align-items-center gap-3 p-2 rounded-3 "
                                                                    >
                                                                        <div
                                                                            className="d-flex w-100 justify-content-between align-items-center flex-row px-3 text-secondary">
                                                                            <div className="d-flex align-items-center gap-2 w-100">
                                                                                <img
                                                                                    src={bookerProfilePics[booking.bookerId] || defaultProfilePic}
                                                                                    alt="Passenger"
                                                                                    className="rounded-circle"
                                                                                    width="40"
                                                                                    height="40"
                                                                                />
                                                                                <span className="fw-semibold text-dark">{booking.bookerName}</span>

                                                                            </div>
                                                                            <div className="fw-semibold">
                                                                                <i className="fa-solid fa-chevron-right"></i>
                                                                            </div>
                                                                        </div>
                                                                    </Link>
                                                                </div>

                                                                <div className="row">
                                                                    <div className="col-md-6 p-0">
                                                                        <div className="row g-3">
                                                                            <div className="col-6">
                                                                                <div
                                                                                    className="text-muted small mb-1">Booked
                                                                                    Seats
                                                                                </div>
                                                                                <div
                                                                                    className="fw-semibold">{booking.bookedSeats}</div>
                                                                            </div>
                                                                            <div className="col-6">
                                                                                <div
                                                                                    className="text-muted small mb-1">Total
                                                                                    Price
                                                                                </div>
                                                                                <div
                                                                                    className="fw-semibold">{booking.totalPrice},00
                                                                                    MKD
                                                                                </div>
                                                                            </div>
                                                                            <div className="col-6">
                                                                                <div
                                                                                    className="text-muted small mb-1">Payment
                                                                                    Method
                                                                                </div>
                                                                                <div
                                                                                    className="fw-semibold">{booking.paymentMethod}</div>
                                                                            </div>
                                                                            <div className="col-6">
                                                                                <div
                                                                                    className="text-muted small mb-1">Booking
                                                                                    Status
                                                                                </div>
                                                                                <span
                                                                                    className={`badge fw-medium ${
                                                                                        booking.bookingStatus === "CONFIRMED"
                                                                                            ? "bg-info-subtle text-info"
                                                                                            :  booking.bookingStatus === "CANCELLED"
                                                                                                ? "bg-danger-subtle text-danger"
                                                                                                : "bg-warning-subtle text-warning"
                                                                                    }`}
                                                                                >
                                                                                {booking.bookingStatus}
                                                                                </span>
                                                                            </div>
                                                                            <div className="col-12">
                                                                                <div className="text-muted small mt-2">
                                                                                    <i>Booked
                                                                                        on {formatDateTime(booking.bookingTime, "PPPp")}</i>
                                                                                </div>
                                                                            </div>
                                                                        </div>
                                                                    </div>

                                                                    <div className="col-md-6 p-0">
                                                                        <p className="text-muted small mb-1">Message</p>
                                                                        <p className="border small rounded-3 p-3 bg-light text-dark fst-italic">
                                                                            {booking.message}
                                                                        </p>

                                                                        {(ride.status === 'FINISHED' && booking.checkInStatus === 'NOT_CHECKED_IN' && booking.bookingStatus !== 'CANCELLED') && (
                                                                            <div className="d-flex justify-content-end mt-4">
                                                                                <button className="btn btn-light p-2 color-danger fw-bold text-danger">
                                                                                    Report absence
                                                                                </button>
                                                                            </div>
                                                                            )
                                                                        }
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    ))}
                                                </div>
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </>
    )
}