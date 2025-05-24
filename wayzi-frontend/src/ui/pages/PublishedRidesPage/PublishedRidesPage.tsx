import {useEffect} from "react";
import {fetchPublishedRides, setPagination, setFilter} from "../../../redux/slices/publishedRideSlice.ts";
import {PaginationSchemaType} from "../../../schemas/paginationSchema.ts";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../../redux/store.ts";
import {useAsyncThunkHandler} from "../../../hooks/useAsyncThunkHandler.ts";
import {NoPublishedRides} from "../../components/empty-states/NoPublishedRides.tsx";
import {Ride, updateRideStatus} from "../../../redux/slices/rideSlice.ts";
import {PublishedRideCard} from "../../components/published-rides/PublishedRideCard/PublishedRideCard.tsx";
import {RidesPagination} from "../../components/shared/Pagination/Pagination.tsx";
import {StatusFilterForm} from "../../components/shared/StatusFilterForm/StatusFilterForm.tsx";
import {rideStatusFilters} from "../../../constants/filters.ts";


export const PublishedRidesPage = () => {

    const dispatch = useDispatch<AppDispatch>();
    const publishedRidesState = useSelector((state: RootState) => state.publishedRides);
    const {publishedRides, filter, pagination} = publishedRidesState
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        // dispatch(fetchPublishedRides({...filter, ...pagination}))
        handleThunk(dispatch, fetchPublishedRides, {...filter, ...pagination}, () => {})

    }, [dispatch]);

    const handlePageChange = async (newPagination: PaginationSchemaType) => {

        handleThunk(dispatch, fetchPublishedRides, {...filter, ...newPagination}, () => {
            dispatch(setPagination(newPagination))
        })

        // const resultAction = await dispatch(fetchPublishedRides({...filter, ...pagination}));
        //
        // if (updateRideStatus.fulfilled.match(resultAction)) {
        //     dispatch(setPagination(newPagination))
        // }

    };


    return (
        <div className="page-wrapper">
            <div className="container mt-4 mb-5">

                {!loading && (success || error) && publishedRides.length === 0 &&  filter.status === null && (
                    <NoPublishedRides/>
                )}

                {((publishedRides.length > 0) || (publishedRides.length == 0 && filter.status !== null)) && (

                    <>
                        <div className="container w-75 responsive-text p-0">
                            <h2 className="fw-bold text-dark mb-4">Published Rides</h2>

                            <StatusFilterForm
                                filters={rideStatusFilters}
                                state={publishedRidesState}
                                action={fetchPublishedRides}
                                setFilter={setFilter}
                                setPagination={setPagination}
                            />


                            <div className="row row-cols-1 g-3">
                                {publishedRides.length > 0 && (
                                    <>
                                        {publishedRides.map((ride: Ride) => (
                                            <PublishedRideCard key={ride.id} ride={ride}/>
                                        ))}


                                    </>
                                )}
                            </div>
                            <RidesPagination
                                onPageChange={handlePageChange}
                                setPagination={setPagination}
                                state={publishedRidesState}
                            />
                        </div>
                    </>
                )}
            </div>
        </div>
    )
}