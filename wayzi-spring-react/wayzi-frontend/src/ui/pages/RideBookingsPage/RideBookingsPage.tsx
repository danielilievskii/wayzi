import {RidesPagination} from "../../components/shared/Pagination/Pagination.tsx";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {useEffect} from "react";
import {PaginationSchemaType} from "../../../schemas/paginationSchema.ts";
import {fetchRideBookings, RideBooking, setPagination, setFilter} from "../../../redux/slices/rideBookingSlice.ts";
import {NoRideBookings} from "../../components/empty-states/NoRideBookings.tsx";
import {RideBookingCard} from "../../components/ride-bookings/RideBookingCard/RideBookingCard.tsx";
import {rideBookingStatusFilters} from "../../../constants/filters.ts";
import {StatusFilterForm} from "../../components/shared/StatusFilterForm/StatusFilterForm.tsx";

export const RideBookingsPage = () => {

    const dispatch = useDispatch<AppDispatch>();
    const rideBookingsState = useSelector((state: RootState) => state.rideBookings);
    const {rideBookings, filter, pagination} = rideBookingsState
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        handleThunk(dispatch, fetchRideBookings, {...filter, ...pagination}, () => {})

    }, [dispatch]);

    const handlePageChange = (newPagination: PaginationSchemaType) => {

        handleThunk(dispatch, fetchRideBookings, {...filter, ...newPagination}, () => {
            dispatch(setPagination(newPagination));
        })
    };

    return (

        <div className="page-wrapper">
            <div className="container mt-4 mb-5">

                {!loading && (success || error) && rideBookings.length === 0 && (
                    <NoRideBookings/>
                )}

                {rideBookings.length > 0 && (
                    // <RideBookings rideBookings={rideBookings} state={rideBookingsState} handlePageChange={handlePageChange} />

                    <div className="container w-75 responsive-text p-0">
                        <h2 className="fw-bold text-dark mb-4">Bookings</h2>

                        <StatusFilterForm
                            filters={rideBookingStatusFilters}
                            state={rideBookingsState}
                            action={fetchRideBookings}
                            setFilter={setFilter}
                            setPagination={setPagination}
                        />

                        <div className="row row-cols-1 g-3">
                            <>
                                {rideBookings.map((rideBooking: RideBooking) => (
                                    <RideBookingCard key={rideBooking.rideBookingId} rideBooking={rideBooking}/>
                                ))}


                            </>
                        </div>
                        <RidesPagination
                            onPageChange={handlePageChange}
                            setPagination={setPagination}
                            state={rideBookingsState}
                        />
                    </div>
                )}

            </div>
        </div>
    )
}