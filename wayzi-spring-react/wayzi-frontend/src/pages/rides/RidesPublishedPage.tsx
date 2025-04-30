import {useEffect} from "react";
import {fetchPublishedRides, setPagination, setFilter} from "../../redux/slices/publishedRideSlice.ts";
import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {useDispatch, useSelector} from "react-redux";
import {AppDispatch, RootState} from "../../redux/store.ts";
import {useAsyncThunkHandler} from "../../hooks/useAsyncThunkHandler.ts";
import {RidesFilterForm} from "../../components/rides/RidesFilterForm.tsx";
import {RidesPagination} from "../../components/Pagination.tsx";
import {PublishedRideCard} from "../../components/rides/PublishedRideCard.tsx";
import {NoPublishedRides} from "../../components/rides/NoPublishedRides.tsx";
import {NoRides} from "../../components/rides/NoRides.tsx";


export const RidesPublishedPage = () => {

    const dispatch = useDispatch<AppDispatch>();
    const publishedRidesState = useSelector((state: RootState) => state.publishedRides);
    const {publishedRides, filter, pagination} = publishedRidesState
    const {handleThunk, loading, success, error} = useAsyncThunkHandler();

    useEffect(() => {
        const {departureLocationName, arrivalLocationName, ...restFilter} = filter || {};
        // dispatch(fetchFilteredRides({...restFilter, ...pagination}));
        handleThunk(dispatch, fetchPublishedRides, {...restFilter, ...pagination}, () => {
        })

    }, [dispatch]);

    const handlePageChange = (newPagination: PaginationSchemaType) => {
        const {departureLocationName, arrivalLocationName, ...restFilter} = filter || {};
        // dispatch(fetchFilteredRides({ ...restFilter, ...newPagination }));
        handleThunk(dispatch, fetchPublishedRides, {...restFilter, ...newPagination}, () => {
        })

    };

    return (
        <div className="page-wrapper">
            <div className="container mt-4 mb-5">

                {/*{loading && (*/}
                {/*    <div>Loading</div>*/}
                {/*)}*/}


                {publishedRides.length > 0 && (
                    <>
                        <RidesFilterForm
                            state={publishedRidesState}
                            action={fetchPublishedRides}
                            setFilter={setFilter}
                            setPagination={setPagination}
                        />

                        <div className="container w-75 responsive-text p-0">
                            <div className="row row-cols-1 g-3">
                                {publishedRides.length > 0 && (
                                    <>
                                        {publishedRides.map((ride) => (
                                            <PublishedRideCard key={ride.id} ride={ride}/>
                                        ))}

                                        <RidesPagination
                                            onPageChange={handlePageChange}
                                            setPagination={setPagination}
                                            state={publishedRidesState}
                                        />
                                    </>
                                )}
                            </div>
                        </div>
                    </>
                )}

                {!loading && (success || error) && publishedRides.length === 0 && (
                    <NoPublishedRides/>
                )}




            </div>
        </div>
    )
}