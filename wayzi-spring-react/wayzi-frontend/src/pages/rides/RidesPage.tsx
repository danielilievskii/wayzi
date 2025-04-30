import {useEffect, useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../redux/store.ts";
import {fetchFilteredRides, Ride, setFilter, setPagination} from "../../redux/slices/rideSlice.ts";
import {Link} from "react-router-dom";
import "../../styles/rides.css";
import {formatDateTime} from "../../utils/dateUtils.ts";
import {isSameDay, parseISO, isAfter} from 'date-fns';
import {RidesFilterForm} from "../../components/rides/RidesFilterForm.tsx";
import {RidesPagination} from "../../components/Pagination.tsx";

import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {useAsyncThunkHandler} from "../../hooks/useAsyncThunkHandler.ts";
import {RideCard} from "../../components/rides/RideCard.tsx";
import {NoRides} from "../../components/rides/NoRides.tsx";

export const RidesPage = () => {
    const dispatch = useDispatch<AppDispatch>();

    const ridesState = useSelector((state: RootState) => state.rides);
    const {rides, filter, pagination} = ridesState;

    const [ridesOnDate, setRidesOnDate] = useState<Ride[]>([])
    const [ridesAfterDate, setRidesAfterDate] = useState<Ride[]>([])

    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        const {departureLocationName, arrivalLocationName, ...restFilter} = filter || {};

        handleThunk(dispatch, fetchFilteredRides, {...restFilter, ...pagination}, () => {})

    }, [dispatch]);

    useEffect(() => {
        if (!rides || rides.length === 0 || !filter?.date) return;

        const filterDate = filter?.date

        const onDate = rides.filter((ride: Ride) => isSameDay(ride.departureTime.split("T")[0], filterDate))
        const afterDate = rides.filter((ride: Ride) => isAfter(ride.departureTime.split("T")[0], filterDate))

        setRidesOnDate(onDate);
        setRidesAfterDate(afterDate);
    }, [ridesState]);


    const handlePageChange = (newPagination: PaginationSchemaType) => {
        const {departureLocationName, arrivalLocationName, ...restFilter} = filter || {};
        // dispatch(fetchFilteredRides({ ...restFilter, ...newPagination }));
        handleThunk(dispatch, fetchFilteredRides, {...restFilter, ...newPagination}, () => {})

    };


    return (
        <div className="page-wrapper">
            <div className="container mt-4 mb-5">
                <RidesFilterForm
                    state={ridesState}
                    action={fetchFilteredRides}
                    setFilter={setFilter}
                    setPagination={setPagination}
                />

                <div className="container w-75 responsive-text p-0">
                    <div className="row row-cols-1 g-3">
                        {ridesOnDate.length > 0 && (
                            <>
                                <div className="row mb-0">
                                    <span className="text-dark-emphasis fw-bold fs-5 px-2 py-1">
                                    {formatDateTime(filter.date, "EEEE, d MMMM")}
                                    </span>
                                </div>
                                {ridesOnDate.map((ride) => (
                                    <RideCard key={ride.id} ride={ride} dateFlag={false}/>
                                ))}

                                <RidesPagination
                                    onPageChange={handlePageChange}
                                    setPagination={setPagination}
                                    state={ridesState}
                                />
                            </>
                        )}


                        {ridesOnDate.length === 0 && ridesAfterDate.length > 0 && (
                            <>
                                <div>
                                    <p className="text-center">
                                        There aren't any rides scheduled on this date. Showing rides after the selected date.
                                    </p>
                                </div>

                                {ridesAfterDate.map((ride) => (
                                    <RideCard key={ride.id} ride={ride} dateFlag={true}/>
                                ))}

                                <RidesPagination
                                    onPageChange={handlePageChange}
                                    setPagination={setPagination}
                                    state={ridesState}
                                />
                            </>
                        )}

                        {!loading && (success || error)&& ridesOnDate.length === 0 && ridesAfterDate.length === 0 && (
                            <NoRides/>
                        )}
                    </div>

                </div>
            </div>
        </div>
    );
};
