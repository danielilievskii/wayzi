// import {RideBookingCard} from "../RideBookingCard/RideBookingCard.tsx";
// import {RidesPagination} from "../../shared/Pagination/Pagination.tsx";
// import {setPagination} from "../../../../redux/slices/rideBookingSlice.ts";
// import {useSelector} from "react-redux";
// import {RootState} from "../../../../redux/store.ts";
// import {RideBooking} from "../../../../redux/slices/rideBookingSlice.ts";
// import {PaginationSchemaType} from "../../../../schemas/paginationSchema.ts";
//
// interface RideBookingsProps {
//     rideBookings: RideBooking[];
//     state: any;
//     handlePageChange: (newPagination: PaginationSchemaType) => void;
// }
//
// export const RideBookings: React.FC<RideBookingsProps> = (props) => {
//     const {rideBookings, state, handlePageChange} = props;
//
//     return (
//         <div className="container w-75 responsive-text p-0">
//             <div className="row row-cols-1 g-3">
//                 <>
//                     {rideBookings.map((rideBooking: RideBooking) => (
//                         <RideBookingCard key={rideBooking.rideBookingId} rideBooking={rideBooking}/>
//                     ))}
//
//                     <RidesPagination
//                         onPageChange={handlePageChange}
//                         setPagination={setPagination}
//                         state={state}
//                     />
//                 </>
//             </div>
//         </div>
//     )
// }