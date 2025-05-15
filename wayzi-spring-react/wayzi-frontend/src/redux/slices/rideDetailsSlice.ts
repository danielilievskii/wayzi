import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance";
import { Vehicle } from "./vehicleSlice";
import { Location } from "./locationSlice";

export interface RideStop {
    id: number;
    location: Location;
    stopTime: string;
    stopOrder: number;
}

export interface RideBookingsUser {
    bookerName: string;
    bookerId: string;
    bookedSeats: number;
}

export interface RideDetails {
    id: string;
    driverName: string
    driverId: string
    departureLocation: Location;
    departureTime: string;
    arrivalLocation: Location;
    arrivalTime: string;
    vehicle: Vehicle;
    availableSeats: number;
    pricePerSeat: number;
    rideStatus: string;
    rideStops: RideStop[];
    rideBookingUsers: RideBookingsUser[];
}

export interface RideDetailsState {
    ride: RideDetails | null;

    loading: boolean;
    error: string | null;
}

const initialState: RideDetailsState = {
    ride: null,
    loading: false,
    error: null,
};

export const fetchRideDetails = createAsyncThunk<RideDetails, void, { rejectValue: string }>(
    'rides/fetchRideDetails',
    async (rideId, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.get('/rides/' + rideId);
            return res.data;
        } catch (err: any) {
            console.log(err)
            return rejectWithValue(err.response?.data || 'Failed to fetch ride details');
        }
    }
);

const rideDetailsSlice = createSlice({
    name: 'rideDetails',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            // FILTER RIDES
            .addCase(fetchRideDetails.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchRideDetails.fulfilled, (state, action: PayloadAction<RideDetails>) => {
                state.loading = false;
                state.ride = action.payload;
            })
            .addCase(fetchRideDetails.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch ride details';
            })

    },
});

export default rideDetailsSlice.reducer;
