import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import rideBookingRepository from "../../repository/rideBookingRepository.ts";
import {Location} from "./locationSlice.ts";

export interface RideBookingUserDetails{
    bookerName: string;
    bookerId: string;
    bookedSeats: number;
    totalPrice: number;
    message: string;
    paymentMethod: string;
    bookingTime: string;
    bookingStatus: string;
    checkInStatus: string;

}

export interface RideBookers{
    id: string;
    departureLocation: Location;
    arrivalLocation: Location;
    availableSeats: number;
    pricePerSeat: number;
    status: string;
    bookers: RideBookingUserDetails[];
}

export interface RideBookersState {
    ride: RideBookers | null;
    loading: boolean;
    error: string | null;
}

const initialState: RideBookersState = {
    ride: null,
    loading: true,
    error: null,
};

export const fetchBookersByRideId = createAsyncThunk<RideBookers, string, { rejectValue: string }>(
    'rides/fetchBookersByRideId',
    async (id, { rejectWithValue }) => {

        return rideBookingRepository.findBookersById(id)
            .then((response) => {
                console.log(response.data)
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch ride bookers.');
            })
    }
);

const rideBookersSlice = createSlice({
    name: 'rideBookers',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchBookersByRideId.pending, () => {})
            .addCase(fetchBookersByRideId.fulfilled, (state, action: PayloadAction<RideBookers>) => {
                state.loading = false;
                state.ride = action.payload;
            })
            .addCase(fetchBookersByRideId.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong.';
            })

    },
});

export default rideBookersSlice.reducer;
