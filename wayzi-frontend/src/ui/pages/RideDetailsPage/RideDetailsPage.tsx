import {useUser} from "../../../context/UserContext.tsx";
import {useNavigate, useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect, useState} from "react";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";

import "leaflet/dist/leaflet.css"
import {formatDateTime} from "../../../utils/dateUtils.ts";
import {MapContainer, TileLayer} from 'react-leaflet'
import MapContent from "../../components/rides/MapContent/MapContent.tsx";
import "../../components/modal/Modal.css"
import {Link} from "react-router-dom";
import {fetchRideDetails} from "../../../redux/slices/rideDetailsSlice.ts";
import {downloadProfilePic} from "../../../redux/slices/profilePicSlice.ts";
import defaultProfilePic from "../../../../public/assets/images/default-profile-pic.png";


export const RideDetailsPage = () => {
    const {currentUser} = useUser()
    const navigate = useNavigate();

    const dispatch = useDispatch<AppDispatch>();
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    const {rideId} = useParams();
    const {ride} = useSelector((state: RootState) => state.rideDetails);

    const {pictures} = useSelector((state: RootState) => state.profilePics);
    const [driverProfilePic, setDriverProfilePic] = useState(pictures[ride?.driverId]);


    const [bookerProfilePics, setBookerProfilePics] = useState<Record<string, string>>({});

    useEffect(() => {
        if (rideId && (!ride || ride.id !== rideId)) {
            handleThunk(dispatch, fetchRideDetails, rideId, () => {});
        }
    }, [rideId, dispatch]);

    useEffect(() => {
        if (ride) {
            if (!pictures[ride.driverId]) {
                dispatch(downloadProfilePic(ride.driverId))
            }
        }
    }, [pictures, ride]);

    useEffect(() => {
        if (ride && pictures[ride.driverId]) {
            setDriverProfilePic(pictures[ride.driverId]);
        }
    }, [ride, pictures]);


    useEffect(() => {
        if (ride) {
            const missingPicUserIds = new Set<string>();

            ride.rideBookingUsers.forEach((user) => {
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

        ride.rideBookingUsers.forEach(user => {
            const id = String(user.bookerId);
            updatedPics[id] = pictures[id];
        });

        setBookerProfilePics(updatedPics);
    }, [pictures, ride]);


    if (loading || !ride) {
        return <div>Loading...</div>;
    }

    return (

        <div className="page-wrapper">
            <div className="container py-5">
                <div className="row g-4 justify-content-center">

                    {/* Ride Overview Card */}
                    <div className="col-lg-8">
                        <div className="floating-card p-4">
                            <h5 className="fw-bold text-secondary mb-3">
                                {formatDateTime(ride.departureTime, 'PPPP')}
                            </h5>

                            <div className="row g-3">
                                {/* Route Info */}
                                <div className="col-md-4">
                                    <div className="d-flex flex-column justify-content-between h-100 py-3">
                                        <div className="position-relative ps-4">

                                            {/* Vertical timeline line */}
                                            <div
                                                className="position-absolute rounded translate-middle-x"
                                                style={{
                                                    top: '7px',
                                                    bottom: '26px',
                                                    width: '4px',
                                                    backgroundColor: '#ced4da'
                                                }}
                                            ></div>

                                            {/* Departure point */}
                                            <div className="mb-4 position-relative d-flex gap-3 align-items-start">
                                                {/* Dot on the line */}
                                                <div
                                                    className="position-absolute start-0 translate-middle-x"
                                                    style={{
                                                        top: '5px',
                                                        width: '14px',
                                                        height: '14px',
                                                        border: '2px solid #343a40',
                                                        borderRadius: '50%',
                                                        backgroundColor: '#343a40',
                                                        zIndex: 1
                                                    }}
                                                ></div>

                                                <div className="ms-4">
                                                    <div
                                                        className="fw-semibold text-dark">{ride.departureLocation.displayName}
                                                    </div>
                                                    <div
                                                        className="text-muted small">{formatDateTime(ride.departureTime, "hh:mm a")} • <i>{ride.departureAddress}</i>
                                                    </div>
                                                </div>
                                            </div>

                                            {/* Ride stops */}
                                            {ride.rideStops.map((stop, idx) => (
                                                <div key={idx}
                                                     className="mb-4 position-relative d-flex gap-3 align-items-start">
                                                    <div
                                                        className="position-absolute start-0 translate-middle-x"
                                                        style={{
                                                            top: '5px',
                                                            width: '14px',
                                                            height: '14px',
                                                            border: '2px solid #6c757d',
                                                            borderRadius: '50%',
                                                            backgroundColor: 'white',
                                                            zIndex: 1
                                                        }}
                                                    ></div>

                                                    <div className="ms-4">
                                                        <div
                                                            className="text-body-secondary">{stop.location.displayName}
                                                        </div>
                                                        {/*<div*/}
                                                        {/*    className="text-muted small">{formatDateTime(stop.stopTime, "hh:mm a")} • <i>{stop.stopAddress}</i>*/}
                                                        {/*</div>*/}
                                                    </div>
                                                </div>
                                            ))}

                                            {/* Arrival point */}
                                            <div className="position-relative d-flex gap-3 align-items-start">
                                                <div
                                                    className="position-absolute start-0 translate-middle-x"
                                                    style={{
                                                        top: '5px',
                                                        width: '14px',
                                                        height: '14px',
                                                        border: '2px solid #212529',
                                                        borderRadius: '50%',
                                                        backgroundColor: '#212529',
                                                        zIndex: 1
                                                    }}
                                                ></div>

                                                <div className="ms-4">
                                                    <div
                                                        className="fw-semibold text-dark">{ride.arrivalLocation.displayName}</div>
                                                    <div
                                                        className="text-muted small">{formatDateTime(ride.arrivalTime, "hh:mm a")} • <i>{ride.arrivalAddress}</i></div>
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            Price per seat: <span className="fw-bold">{ride.pricePerSeat},00 MKD</span>
                                        </div>
                                    </div>

                                </div>


                                {/* Map */}
                                <div className="col-md-8">
                                    <MapContainer
                                        center={[41.6086, 21.7453]}
                                        zoom={8}
                                        worldCopyJump={false}
                                        className="rounded"
                                        style={{width: '100%', height: "400px", position: 'relative', zIndex: '0'}}
                                    >
                                        <TileLayer
                                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
                                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        />
                                        <MapContent ride={ride}/>
                                    </MapContainer>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Side Card with Driver Info & Booking */}
                    <div className="col-lg-4">
                        <div
                            className="floating-card shadow-sm border-0 p-4 h-100 d-flex flex-column justify-content-between">
                            {/* Driver */}
                            <div>
                                <div className="d-flex align-items-center gap-3 mb-3">
                                    <img
                                        src={driverProfilePic || defaultProfilePic}
                                        alt="Driver"
                                        className="rounded-circle"
                                        width="60"
                                        height="60"
                                    />
                                    <div>
                                        <h6 className="mb-0">{ride.driverName}</h6>
                                        <small className="text-muted">Driver</small>
                                    </div>
                                </div>
                                <hr/>

                                {/* Vehicle */}
                                <div className="mb-3">
                                    <i className={`fa-solid ${getVehicleIcon(ride.vehicle.type)} text-secondary`}></i>
                                    <span className="ms-2">{ride.vehicle.model} {ride.vehicle.brand}</span> -
                                    <span className="ms-2">{ride.vehicle.color}</span>
                                </div>

                                {/* Trust Indicators */}
                                <div className="text-muted d-flex flex-column gap-2">
                                    <div>
                                        <i className="fa-solid fa-circle-check text-info"/>
                                        <span className="ms-2">Verified phone number (placeholder)</span>
                                    </div>
                                    <div>
                                        <i className="fa-regular fa-calendar-days text-success"/>
                                        <span className="ms-2">Rarely cancels rides (placeholder)</span>
                                    </div>
                                </div>
                            </div>

                            {/* Status & Book Button */}
                            <div className="mt-4">
                                <div className="text-muted mb-3">
                                    {renderStatusMessage(ride.rideStatus)}
                                </div>
                                {(ride.rideStatus === "PENDING" || ride.rideStatus === "CONFIRMED") && (
                                    <>
                                        <button
                                            className="btn btn-info text-white fw-bold w-100"
                                            onClick={() => navigate(`/rides/${ride.id}/book`)}
                                        >
                                            Book Now
                                        </button>
                                    </>
                                )}
                            </div>
                        </div>
                    </div>

                    {/* Passengers Section */}
                    {ride.rideBookingUsers && ride.rideBookingUsers.length > 0 && (
                        <div className="col-lg-8">
                            <div className="floating-card shadow-sm border-0 p-4">
                                <h6 className="fw-bold text-secondary mb-3">Passengers</h6>
                                <div className="row g-3">
                                    {ride.rideBookingUsers.map((user, index) => (
                                        <Link
                                            key={index}
                                            to={`/profile`}
                                            className="btn btn-light border-0 text-decoration-none col-12 d-flex align-items-center gap-3 p-2 rounded-3 "
                                        >
                                            <div
                                                className="d-flex w-100 justify-content-between align-items-center flex-row px-3 text-secondary">
                                                <div className="d-flex align-items-center gap-2 w-100">
                                                    <img
                                                        src={bookerProfilePics[user.bookerId] || defaultProfilePic}
                                                        alt="Passenger"
                                                        className="rounded-circle"
                                                        width="40"
                                                        height="40"
                                                    />
                                                    <span className="fw-semibold text-dark">{user.bookerName}</span>
                                                    <span
                                                        className="fw-semibold badge bg-info">{user.bookedSeats} seats</span>
                                                </div>
                                                <div className="fw-semibold">
                                                    <i className="fa-solid fa-chevron-right"></i>
                                                </div>
                                            </div>

                                        </Link>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}

                </div>
            </div>
        </div>

    );

}

const getVehicleIcon = (type: string) => {
    switch (type) {
        case "AUTOMOBILE":
            return "fa-car-side";
        case "MOTORBIKE":
            return "fa-motorcycle";
        case "BUS":
            return "fa-bus";
        case "VAN":
            return "fa-van-shuttle";
        default:
            return "fa-car";
    }
};

const renderStatusMessage = (status: string) => {
    switch (status) {
        case "PENDING":
            return "The ride is still awaiting confirmation from the driver.";
        case "CONFIRMED":
            return "This ride has been confirmed. Hurry up and book your seat before it's full!";
        case "CANCELLED":
            return "This ride has been cancelled. Unfortunately, it is no longer available.";
        case "STARTED":
            return "The ride has already started. You can no longer book for this ride.";
        case "FINISHED":
            return "The ride has been completed. Check out other available rides!";
        default:
            return "";
    }
};