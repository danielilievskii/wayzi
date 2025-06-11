import {Link} from "react-router-dom";
import "../../../styles/rides.css"
import {formatDateTime} from "../../../../utils/dateUtils.ts";
import {updateRideStatus} from "../../../../redux/slices/rideSlice.ts";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../../redux/store.ts";

import {fetchPublishedRides} from "../../../../redux/slices/publishedRideSlice.ts";

export const PublishedRideCard = (props) => {
    const {ride} = props
    const dispatch = useDispatch<AppDispatch>();

    const {updateRideStatusError, updateRideStatusLoading} = useSelector((state: RootState) => state.rides)
    const publishedRidesState = useSelector((state: RootState) => state.publishedRides);
    const {filter, pagination} = publishedRidesState

    const updateStatus = async (status: string) => {
        const resultAction = await dispatch(updateRideStatus({id: ride.id, newStatus: status}));

        if (updateRideStatus.fulfilled.match(resultAction)) {
            dispatch(fetchPublishedRides({...filter, ...pagination}))
        }

    }

    return (
        <div key={ride.id}>
            <div className="ride-card ride-card-border">
                <Link to={`/rides/${ride.id}`} className="text-decoration-none">

                    <div className="card border-0  px-2 py-3 py-sm-3 px-sm-3 py-md-4 px-md-4">

                        <div className="row align-items-center justify-content-between">
                            {/*Ride Details*/}
                            <div className="col-md-8">
                                <div className="row mb-3">
                                    <div className="d-flex align-items-center gap-2">
                                        <div className="fw-semibold text-dark-emphasis">
                                            {ride.rideStatus === "PENDING" && (
                                                <span className="status-bar">PENDING</span>
                                            )}
                                            {ride.rideStatus === "CONFIRMED" && (
                                                <span className="status-bar">CONFIRMED</span>
                                            )}
                                            {ride.rideStatus === "CANCELLED" && (
                                                <span className="status-bar">CANCELLED</span>
                                            )}
                                            {ride.rideStatus === "STARTED" && (
                                                <span className="status-bar">STARTED</span>
                                            )}
                                            {ride.rideStatus === "FINISHED" && (
                                                <span className="status-bar">FINISHED</span>
                                            )}
                                        </div>
                                        <span className="text-dark-emphasis fw-bold">
                                            {formatDateTime(ride.departureTime, "EEEE, d MMMM, yyyy")}
                                        </span>
                                    </div>

                                </div>
                                <div className="row mb-2">
                                    <div className="d-flex align-items-center gap-2">
                                        <h5 className="text-dark-emphasis">
                                            {ride.departureLocation.displayName}
                                        </h5>
                                        <span className="bg-light px-2 py-1 rounded">
                                            {formatDateTime(ride.departureTime, "hh:mm a")}
                                        </span>

                                        <i className="fa-solid fa-arrow-right"></i>
                                        <h5 className="text-dark-emphasis">
                                            {ride.arrivalLocation.displayName}
                                        </h5>
                                        <span className="bg-light px-2 py-1 rounded">
                                            {formatDateTime(ride.arrivalTime, "hh:mm a")}
                                        </span>
                                    </div>
                                </div>
                                <div className="row">
                                    <div className="d-flex align-items-center align-content-end gap-3">

                                        <p>
                                            {ride.vehicle.type === "AUTOMOBILE" && (
                                                <i className="fa-solid fa-car"></i>
                                            )}
                                            {ride.vehicle.type === "MOTORBIKE" && (
                                                <i className="fa-solid fa-motorcycle"></i>
                                            )}
                                            {ride.vehicle.type === "BUS" && (
                                                <i className="fa-solid fa-bus"></i>
                                            )}
                                            {ride.vehicle.type === "VAN" && (
                                                <i className="fa-solid fa-van-shuttle"></i>
                                            )}
                                            <span className="mx-2 color-gray">
                                                {ride.vehicle.model} {ride.vehicle.brand}
                                            </span>
                                        </p>

                                        <p>
                                            <i className="fa-solid fa-users"></i>
                                            <span className="mx-2 color-gray">
                                            <span>{ride.availableSeats}</span>
                                            <span>
                                                {ride.availableSeats == 1 ? ("booked seat") : ("booked seats")}
                                            </span>
                                    </span>
                                        </p>

                                        <p>
                                            <i className="fa-solid fa-coins"></i>
                                            <span className="mx-2 color-gray">
                                        <span>{ride.pricePerSeat}</span>,00 MKD
                                    </span>

                                        </p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </Link>
            </div>
            <div className="d-flex justify-content-evenly rounded p-2 row-gap-2 bg-white">

                {ride.rideStatus == 'PENDING' && (
                    <>
                        <div className="col-md-3">
                            <button
                                onClick={() => {
                                    updateStatus("CONFIRMED")
                                }
                                }
                                className="btn btn-light p-2 fw-bold w-100"
                            >
                                Confirm
                            </button>
                        </div>
                    </>

                )}

                {ride.rideStatus == 'CONFIRMED' && (
                    <div className="col-md-3">
                        <button
                            onClick={() => {
                                updateStatus("STARTED")
                            }}
                            className="btn btn-light p-2 fw-bold w-100"
                        >
                            Start
                        </button>
                    </div>
                )}

                {ride.rideStatus == 'STARTED' && (
                    <div className="col-md-3">
                        <button
                            onClick={() => {
                                updateStatus("FINISHED")
                            }}
                            className="btn btn-light p-2 fw-bold w-100"
                        >
                            Finish
                        </button>
                    </div>
                )}

                <div className="col-md-3">
                    <button
                        className={`btn btn-light p-2 fw-bold w-100`}>View bookings
                    </button>
                </div>

                {(ride.rideStatus == 'PENDING' || ride.rideStatus == 'CONFIRMED') && (
                    <>
                        <div className="col-md-3">
                            <button
                                onClick={() => {
                                    updateStatus("CANCELLED")
                                }}
                                className="btn btn-light p-2 color-danger text-danger fw-bold w-100">
                                Cancel ride
                            </button>
                        </div>
                    </>
                )}

            </div>
        </div>
    )
}