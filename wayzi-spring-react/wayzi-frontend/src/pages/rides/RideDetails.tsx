import {useUser} from "../../context/UserContext.tsx";
import {useNavigate, useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../redux/store.ts";
import {useEffect, useRef, useState} from "react";
import {useAsyncThunkHandler} from "../../hooks/useAsyncThunkHandler.ts";
import axiosInstance from "../../axios/axiosInstance.ts";
import {Ride} from "../../redux/slices/rideSlice.ts";
import L from "leaflet"
import "leaflet/dist/leaflet.css"
import {formatDateTime} from "../../utils/dateUtils.ts";
import {MapContainer, TileLayer} from 'react-leaflet'
import MapContent from "../../components/rides/MapContent.tsx";
import {ModalTest} from "../../components/modal/ModalTest.tsx";
import "../../components/modal/Modal.css"



export const RideDetails = () => {
    const {currentUser} = useUser()

    const navigate = useNavigate();
    const dispatch = useDispatch<AppDispatch>();
    // const { handleThunk, loading, success, error } = useAsyncThunkHandler();

    const {ride_id} = useParams();
    const {rides} = useSelector((state: RootState) => state.rides);
    const [ride, setRide] = useState<Ride | null>(
        rides.find(ride => ride.id === Number(ride_id)) || null
    );

    const [loading, setLoading] = useState(false);


    // Fetch ride if not found in Redux (e.g., page refresh)
    useEffect(() => {
        const fetchRide = async () => {
            try {
                setLoading(true);
                console.log("Fetching ride");
                const res = await axiosInstance.get<Ride>(`/rides/${ride_id}`);
                setRide(res.data)
                console.log(res.data);
            } catch (err) {
                console.error("Failed to fetch vehicle", err);
            } finally {
                setLoading(false);

            }

        };

        if (!ride && ride_id) fetchRide();
    }, [ride, ride_id]);


    if (loading || !ride) {
        return <div>Loading...</div>;
    }

    return (

        <div className="page-wrapper">
            <div className="container py-5">
                <div className="row d-flex justify-content-center gap-3">
                    <div className="col-md-8 floating-card p-3">
                        <h5 className="fw-bold mb-3 text-secondary">
                            {formatDateTime(ride.departureTime, 'PPPP')}
                        </h5>

                        <div className="row">
                            <div className="col-md-3 d-flex flex-column gap-2">
                                <div>
                                <span className="fw-semibold text-dark">
                                    {ride.departureLocation.displayName}
                                </span>
                                    <span className="bg-danger-subtle px-2 py-1 rounded ms-2">
                                    {formatDateTime(ride.departureTime, "hh:mm a")}
                                </span>
                                </div>

                                <div>
                                    <i className="fa-solid fa-arrow-down"/>
                                </div>

                                {ride.rideStops.map((stop, idx) => (
                                    <div key={idx}>
                                        <span style={{color: "gray"}}>{stop.location.displayName}</span>
                                        <div>
                                            <i className="fa-solid fa-arrow-down"/>
                                        </div>
                                    </div>
                                ))}

                                <div>
                                <span className="fw-semibold text-dark">
                                    {ride.arrivalLocation.displayName}
                                </span>
                                    <span className="bg-danger-subtle px-2 py-1 rounded ms-2">
                                    {formatDateTime(ride.arrivalTime, "hh:mm a")}
                                </span>
                                </div>
                            </div>

                            <div className="col-md-9">
                                {ride && (
                                    <MapContainer
                                        center={[41.6086, 21.7453]}
                                        zoom={8}
                                        worldCopyJump={false}
                                        className="rounded"
                                        style={{ width: '100%', height: "400px", position: 'relative', zIndex: '9' }}
                                    >
                                        <TileLayer
                                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                                        />
                                        <MapContent ride={ride}/>
                                    </MapContainer>
                                )}
                            </div>
                        </div>
                    </div>

                    <div className="col-md-3 floating-card p-3 d-flex flex-column justify-content-between">
                        <div>
                            <div className="d-flex align-items-center gap-2 mb-2">
                                <img
                                    src='/assets/defualt-profile-pic.png'
                                    alt={"Driver picture"}
                                    className="rounded-circle"
                                    width="50"
                                    height="50"
                                />
                                <span>{ride.driverName}</span>
                            </div>
                            <hr className="text-muted"/>

                            <div className="d-flex flex-column gap-2">
                                <div>
                                    <i className={`fa-solid ${getVehicleIcon(ride.vehicle.type)} text-secondary`}></i>
                                    <span className="ms-2">{ride.vehicle.model} {ride.vehicle.brand}</span> -
                                    <span className="ms-2">{ride.vehicle.color}</span>
                                </div>

                                <div>
                                    <i className="fa-solid fa-circle-check text-info"/>
                                    <span className="ms-2">Verified phone number</span>
                                </div>

                                <div>
                                    <i className="fa-regular fa-calendar-days" style={{color: "#3EB489"}}/>
                                    <span className="ms-2">Rarely cancels rides</span>
                                </div>
                            </div>
                        </div>

                        <div className="mt-3 text-muted">
                            {renderStatusMessage(ride?.rideStatus)}
                            <div className="mt-2">
                                {/*<button className="btn btn-lg btn-info text-light fw-bold">Book now</button>*/}
                                <ModalTest/>
                            </div>
                        </div>
                    </div>
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