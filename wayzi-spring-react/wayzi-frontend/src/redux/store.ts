import {configureStore} from "@reduxjs/toolkit"
import vehicleReducer from "./slices/vehicleSlice.ts"
import ridesReducer from "./slices/rideSlice.ts";
import publishedRidesReducer from "./slices/publishedRideSlice.ts";
import locationReducer from "./slices/locationSlice.ts";

export const store = configureStore({
    reducer: {
        vehicle: vehicleReducer,
        rides: ridesReducer,
        publishedRides: publishedRidesReducer,
        location: locationReducer
    }
})

export type RootState = ReturnType<typeof store.getState>

// needed for async actions
export type AppDispatch = typeof store.dispatch