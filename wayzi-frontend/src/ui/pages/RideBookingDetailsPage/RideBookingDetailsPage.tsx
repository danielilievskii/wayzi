import {useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {formatDateTime} from "../../../utils/dateUtils.ts";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {QrCodeImageDialog} from "../../components/ride-bookings/QrCodeDialog/QrCodeImgDialog.tsx";
import {fetchBookingDetailsForBooker} from "../../../redux/slices/rideBookingDetailsSlice.ts";
import {downloadProfilePic} from "../../../redux/slices/profilePicSlice.ts";
import defaultProfilePic from "../../../../public/assets/images/default-profile-pic.png"

export const RideBookingDetailsPage = () => {

    const dispatch = useDispatch<AppDispatch>();
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    const {rideBookingId} = useParams();

    const rideBookingsState = useSelector((state: RootState) => state.rideBookingDetails);
    const {rideBooking} = rideBookingsState

    const {pictures} = useSelector((state: RootState) => state.profilePics);
    const [driverProfilePic, setDriverProfilePic] = useState(pictures[rideBooking?.driverId]);

    useEffect(() => {
        if (!rideBooking && rideBookingId) {
            dispatch(fetchBookingDetailsForBooker(rideBookingId))
        }

    }, [rideBooking, dispatch]);

    useEffect(() => {
        if (rideBooking) {
            if (!pictures[rideBooking.driverId]) {
                handleThunk(dispatch, downloadProfilePic, rideBooking.driverId, () => {
                    setDriverProfilePic(pictures[rideBooking.driverId]);
                })
            }
        }

    }, [pictures, rideBooking, dispatch]);


    return (
        <>
            {rideBooking && (
                <div className="page-wrapper">
                    <div className="container py-5">
                        <div className="row justify-content-center">
                            <div className="col-lg-10 col-md-11">

                                <div className="card shadow-sm rounded-4 border-0">
                                    <div className="card-body p-5">

                                        {/* Header */}
                                        <div className="d-flex justify-content-between align-items-center mb-4">
                                            <div>
                                                <h3 className="fw-bold text-dark mb-1">
                                                    Booking Details
                                                </h3>
                                                <div className="text-muted small">
                                                    Booking No: <strong>{rideBooking.rideBookingId}</strong><br />
                                                </div>
                                            </div>
                                            <QrCodeImageDialog qrCodeUrl={rideBooking.qrCodeUrl}/>
                                        </div>

                                        {/* Ride Route Info */}
                                        <div className="mb-4">
                                            <h5 className="fw-semibold text-primary mb-3">
                                                <a
                                                    href={`/rides/${rideBooking.rideId}`}
                                                    className="btn btn-light text-decoration-none text-primary"
                                                >
                                                    {rideBooking.departureLocation.name} ➜ {rideBooking.arrivalLocation.name}
                                                </a>
                                            </h5>

                                            <div className="row g-3">
                                                <div className="col-md-6">
                                                    <label className="text-muted small">Departure</label>
                                                    <div className="fw-medium">
                                                        {formatDateTime(rideBooking.departureTime, 'PPPPp')}
                                                        <div className="text-muted small">📍
                                                            Pickup Address
                                                        </div>
                                                    </div>
                                                </div>
                                                <div className="col-md-6">
                                                    <label className="text-muted small">Arrival</label>
                                                    <div className="fw-medium">
                                                        {formatDateTime(rideBooking.arrivalTime, 'PPPPp')}
                                                        <div className="text-muted small">
                                                            📍 Drop-off Address
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>

                                        <hr/>

                                        {/* Ride Info Summary */}
                                        <div className="row g-4 mt-3">
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Driver</div>
                                                <div className="d-flex align-items-center gap-2 mb-2">
                                                    <img
                                                        src={driverProfilePic || defaultProfilePic}
                                                        alt={"Driver picture"}
                                                        className="rounded-circle"
                                                        width="40"
                                                        height="40"
                                                    />
                                                    <span className="fw-semibold">{rideBooking.driverName}</span>
                                                </div>
                                                {/*<div className="fw-semibold">{rideBooking.driverName}</div>*/}
                                            </div>
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Payment Method</div>
                                                <div className="fw-semibold">{rideBooking.paymentMethod}</div>
                                            </div>
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Booking Status</div>
                                                <span className="badge bg-info-subtle text-info fw-medium">
                                                    {rideBooking.rideBookingStatus}
                                                </span>
                                            </div>
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Booked Seats</div>
                                                <div className="fw-semibold">{rideBooking.bookedSeats}</div>
                                            </div>
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Total Price</div>
                                                <div className="fw-semibold">{rideBooking.totalPrice},00 MKD</div>
                                            </div>
                                            <div className="col-md-4">
                                                <div className="text-muted small mb-1">Check-in Status</div>
                                                <span
                                                    className={`badge fw-medium ${
                                                        rideBooking.checkInStatus === "CHECKED_IN"
                                                            ? "bg-success-subtle text-success"
                                                            : rideBooking.checkInStatus === "NOT_CHECKED_IN"
                                                                ? "bg-warning-subtle text-warning"
                                                                : "bg-danger-subtle text-danger"
                                                    }`}
                                                >
                                                {rideBooking.checkInStatus.replace(/_/g, " ")}
                                                </span>
                                            </div>
                                            <div className="mt-4">
                                                <div className="text-muted small mb-1">Message</div>
                                                <div className="border rounded-3 p-3 bg-light text-dark fst-italic">
                                                    {rideBooking.message}
                                                </div>
                                            </div>
                                            <div className="text-muted small">
                                                <i>Booked on {formatDateTime(rideBooking.bookingTime, 'PPPp')}</i>
                                            </div>
                                        </div>

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