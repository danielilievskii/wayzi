import "./RideBookingCheckInPage.css"
import {useParams} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useEffect} from "react";
import {fetchRideBookingCheckInDetails} from "../../../redux/slices/rideBookingCheckInSlice.ts";
import {CheckIn} from "../../components/ride-bookings/CheckIn/CheckIn.tsx";

export const RideBookingCheckInPage = () => {

    const {rideBookingId} = useParams();

    const dispatch = useDispatch<AppDispatch>()
    const rideBookingCheckInState = useSelector((state: RootState) => state.rideBookingCheckIn);
    const {rideBooking, loadingFetch, errorFetch, loadingCheckIn, errorCheckIn} = rideBookingCheckInState

    useEffect(() => {
        if (!rideBooking && rideBookingId) {
                dispatch(fetchRideBookingCheckInDetails(rideBookingId))
        }

    }, [dispatch, rideBooking, rideBookingId]);


    return (
        <div className="page-wrapper">
            <div className="container py-5">
                {loadingFetch && !rideBooking && (
                    <div>Loading...</div>
                )}

                {!loadingFetch && rideBooking && (
                    <CheckIn rideBooking={rideBooking} loadingCheckIn={loadingCheckIn} errorCheckIn={errorCheckIn} />
                )}

                {!loadingFetch && errorFetch && (
                    <div>{errorFetch}</div>
                )}
            </div>

        </div>

    )
}