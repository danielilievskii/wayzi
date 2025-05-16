import {configureStore} from "@reduxjs/toolkit"
import vehicleReducer from "./slices/vehicleSlice.ts"
import ridesReducer from "./slices/rideSlice.ts";
import publishedRidesReducer from "./slices/publishedRideSlice.ts";
import rideBookingsReducer from "./slices/rideBookingSlice.ts";
import rideBookingDetailsReducer from "./slices/rideBookingDetailsSlice.ts";
import rideBookingCheckInReducer from "./slices/rideBookingCheckInSlice.ts";
import locationReducer from "./slices/locationSlice.ts";
import profilePicReducer from "./slices/profilePicSlice.ts";
import rideDetailsSlice from "./slices/rideDetailsSlice.ts";

export const store = configureStore({
    reducer: {
        vehicle: vehicleReducer,
        rides: ridesReducer,
        rideDetails: rideDetailsSlice,
        publishedRides: publishedRidesReducer,
        rideBookings: rideBookingsReducer,
        rideBookingDetails: rideBookingDetailsReducer,
        rideBookingCheckIn: rideBookingCheckInReducer,
        location: locationReducer,
        profilePics: profilePicReducer,
    }
})

export type RootState = ReturnType<typeof store.getState>

// needed for async actions
export type AppDispatch = typeof store.dispatch