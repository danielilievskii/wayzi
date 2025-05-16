// import {RidesFilterForm} from "../RidesFilterForm/RidesFilterForm.tsx";
// import {fetchPublishedRides, setFilter, setPagination} from "../../../../redux/slices/publishedRideSlice.ts";
// import {PublishedRideCard} from "../PublishedRideCard/PublishedRideCard.tsx";
// import {RidesPagination} from "../../shared/Pagination/Pagination.tsx";
// import {Ride} from "../../../../redux/slices/rideSlice.ts";
//
// export const PublishedRides = (props) => {
//     const {publishedRides, state, handlePageChange} = props
//
//     return (
//         <>
//             <RidesFilterForm
//                 state={state}
//                 action={fetchPublishedRides}
//                 setFilter={setFilter}
//                 setPagination={setPagination}
//             />
//
//             <div className="container w-75 responsive-text p-0">
//                 <div className="row row-cols-1 g-3">
//                     {publishedRides.length > 0 && (
//                         <>
//                             {publishedRides.map((ride: Ride) => (
//                                 <PublishedRideCard key={ride.id} ride={ride}/>
//                             ))}
//
//                             <RidesPagination
//                                 onPageChange={handlePageChange}
//                                 setPagination={setPagination}
//                                 state={state}
//                             />
//                         </>
//                     )}
//                 </div>
//             </div>
//         </>
//     )
// }