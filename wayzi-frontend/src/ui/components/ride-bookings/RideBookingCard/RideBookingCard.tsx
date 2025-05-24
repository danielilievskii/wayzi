import {Link} from "react-router-dom";
import "../../../styles/rides.css"
import {formatDateTime} from "../../../../utils/dateUtils.ts";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../../redux/store.ts";
import {useEffect, useState} from "react";
import {QrCodeImageDialog} from "../QrCodeDialog/QrCodeImgDialog.tsx";
import {cancelRideBooking, fetchRideBookings} from "../../../../redux/slices/rideBookingSlice.ts";

export const RideBookingCard = (props) => {
    const {rideBooking} = props
    const dispatch = useDispatch<AppDispatch>();

    const { pictures } = useSelector((state: RootState) => state.profilePics);
    const [profilePic, setProfilePic] = useState(pictures[rideBooking.driverId] || "/assets/images/default-profile-pic.png");

    useEffect(() => {
        if (pictures[rideBooking.driverId]) {
            setProfilePic(pictures[rideBooking.driverId]);
        }
    }, [pictures, rideBooking.driverId]);

    const {createRideBookingLoading, cancelRideBookingError, filter, pagination} = useSelector((state: RootState) => state.rideBookings)

    const onCancelBooking = async () => {
        const resultAction = await dispatch(cancelRideBooking(rideBooking.rideBookingId));

        if (cancelRideBooking.fulfilled.match(resultAction)) {
            dispatch(fetchRideBookings({...filter, ...pagination}))
        }
    }

    return (
        <div key={rideBooking.rideBookingId}>
            <div className="ride-card ride-card-border">
                <div className="card border-0 px-2 py-3 py-sm-3 px-sm-3 py-md-4 px-md-4">
                    <div className="row align-items-center justify-content-between">
                        {/* Ride Booking Details */}
                        <div className="col-md-8">
                            <div className="row mb-3">
                                <div className="d-flex align-items-center gap-2">
                                    <div className="fw-semibold text-dark-emphasis">

                                        {rideBooking.rideBookingStatus === "CONFIRMED" && (
                                            <span className="status-bar">CONFIRMED</span>
                                        )}
                                        {rideBooking.rideBookingStatus === "CANCELLED" && (
                                            <span className="status-bar">CANCELLED</span>
                                        )}
                                        {rideBooking.rideBookingStatus === "ARCHIVED" && (
                                            <span className="status-bar">FINISHED</span>
                                        )}
                                    </div>
                                    <span className="text-dark-emphasis fw-bold">
                                        {formatDateTime(rideBooking.departureTime, "EEEE, d MMMM, yyyy")}
                                    </span>
                                </div>
                            </div>

                            <div className="row mb-2">
                                <div className="d-flex align-items-center gap-2">
                                    <h5 className="text-dark-emphasis">
                                        {rideBooking.departureLocation.displayName}
                                    </h5>
                                    <span className="bg-light px-2 py-1 rounded">
                                        {formatDateTime(rideBooking.departureTime, "hh:mm a")}
                                    </span>
                                    <i className="fa-solid fa-arrow-right"></i>
                                    <h5 className="text-dark-emphasis">
                                        {rideBooking.arrivalLocation.displayName}
                                    </h5>
                                    <span className="bg-light px-2 py-1 rounded">
                                        {formatDateTime(rideBooking.arrivalTime, "hh:mm a")}
                                    </span>
                                </div>
                            </div>

                            <div className="row">
                                <div className="d-flex align-items-center align-content-end gap-3">
                                    <p>
                                        <img src={profilePic} alt="Driver picture" width="40"
                                             height="40" className="profile-pic"/>
                                        <span className="mx-2 color-gray">{rideBooking.driverName}</span>
                                    </p>
                                    <p>
                                        <i className="fa-solid fa-users"></i>
                                        <span className="mx-2 color-gray">
                                            {rideBooking.bookedSeats} {rideBooking.bookedSeats === 1 ? "seat" : "seats"}
                                        </span>
                                    </p>
                                    <p>
                                        <i className="fa-solid fa-coins"></i>
                                        <span className="mx-2 color-gray">
                                            {rideBooking.totalPrice},00 MKD
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>

                        {/* Optional: Right-side dropdown or action buttons can go here */}
                        <div className="col-md-4 text-end fw-semibold">
                            <QrCodeImageDialog qrCodeUrl={rideBooking.qrCodeUrl}/>
                        </div>
                    </div>

                </div>
            </div>

            <div className="d-flex justify-content-evenly rounded p-2 row-gap-2 bg-white">

                <div className="col-md-3">
                    <Link to={`/rides/bookings/${rideBooking.rideBookingId}`}>
                        <button
                            onClick={() => {
                                // updateStatus("FINISHED")
                            }}
                            className="btn btn-light p-2 fw-bold w-100"
                        >
                            View details
                        </button>

                    </Link>

                </div>



                {rideBooking.rideBookingStatus == 'CONFIRMED' && (
                    <>
                        {/*<div className="col-md-3">*/}
                        {/*    <QrCodeBtnDialog qrCodeUrl={rideBooking.qrCodeUrl}/>*/}
                        {/*</div>*/}

                        <div className="col-md-3">
                            <button
                                onClick={() => {
                                    onCancelBooking()
                                }}
                                className="btn btn-light p-2 color-danger text-danger fw-bold w-100">
                                Cancel booking
                            </button>
                        </div>
                    </>
                )}


                {rideBooking.rideBookingStatus == 'ARCHIVED' && (
                    <>
                        <div className="col-md-3">
                            <button
                                className={`btn btn-light p-2 fw-bold w-100`}>Review
                            </button>
                        </div>
                        <div className="col-md-3">
                            <button className="btn btn-light p-2 color-danger text-danger fw-bold w-100">
                                Report absence
                            </button>
                        </div>
                    </>
                )}

            </div>


        </div>
    )
}