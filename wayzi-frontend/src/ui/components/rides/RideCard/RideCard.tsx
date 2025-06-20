import {Link} from "react-router-dom";
import {formatDateTime} from "../../../../utils/dateUtils.ts";
import {useSelector} from "react-redux";
import {RootState} from "../../../../redux/store.ts";
import {useEffect, useState} from "react";

export const RideCard = (props) => {
    const {ride, dateFlag} = props
    const { pictures } = useSelector((state: RootState) => state.profilePics);
    const [profilePic, setProfilePic] = useState(pictures[ride.driverId] || "/assets/images/default-profile-pic.png");

    useEffect(() => {
        if (pictures[ride.driverId]) {
            setProfilePic(pictures[ride.driverId]);
        }
    }, [pictures, ride.driverId]);

    return (
        <div key={ride.id} className="col-md-12 ride-card p-0">
            <Link to={`/rides/${ride.id}`} className="text-decoration-none">

                <div className="card shadow-sm border-0 px-3 py-2 p-md-4">

                    {dateFlag &&
                        <span className="text-secondary fw-bold">{formatDateTime(ride.departureTime, "EEEE, d MMMM")}</span>
                    }
                    <div className="row justify-content-between align-items-start">
                        <div className="col-md-8">
                            <div className="d-flex align-items-center gap-2 mb-3">
                                <h5>{ride.departureLocation.displayName}</h5>
                                <span className="bg-light px-2 py-1 rounded">
                                    {formatDateTime(ride.departureTime, "hh:mm a")}
                                </span>
                                <i className="fa-solid fa-arrow-right"></i>
                                <h5>{ride.arrivalLocation.displayName}</h5>
                                <span className="bg-light px-2 py-1 rounded">
                                    {formatDateTime(ride.arrivalTime, "hh:mm a")}
                                </span>
                            </div>
                            <div className="d-flex align-items-center gap-3">
                                <p>
                                    <img  src={profilePic} alt="Driver picture" width="40" height="40" className="profile-pic" />
                                    <span className="color-gray mx-2">{ride.driverName}</span>
                                </p>
                                <p>
                                    <i className={`fa-solid ${
                                        ride.vehicle.type === 'AUTOMOBILE' ? 'fa-car' :
                                            ride.vehicle.type === 'MOTORBIKE' ? 'fa-motorcycle' :
                                                ride.vehicle.type === 'BUS' ? 'fa-bus' :
                                                    'fa-van-shuttle'
                                    }`}></i>
                                    <span className="mx-2 color-gray">{ride.vehicle.model} {ride.vehicle.brand}</span>
                                </p>
                                <p>
                                    <i className="fa-solid fa-users"></i>
                                    <span className="mx-2 color-gray">
                                        {ride.availableSeats} {ride.availableSeats === 1 ? "available seat" : "available seats"}
                                    </span>
                                </p>
                            </div>
                        </div>
                        <div className="col-md-3 text-end">
                            <h5 className="fw-bold text-dark-emphasis">
                                {ride.pricePerSeat},00 MKD
                            </h5>
                        </div>
                    </div>
                </div>
            </Link>
        </div>
    )

}