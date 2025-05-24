import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance";
import {RideBooking} from "./rideBookingSlice.ts";
import rideBookingRepository from "../../repository/rideBookingRepository.ts";

interface RideBookingCheckIn {
    rideBookingId: string;
    bookerName: string;
    bookerId: string;
    paymentMethod: string;
    rideBookingStatus: string;
    checkInStatus: string;
    bookedSeats: number;
}

export interface RideBookingCheckInState {
    rideBooking: RideBookingCheckIn | null;
    loadingFetch: boolean;
    errorFetch: string | null;
    loadingCheckIn: boolean;
    errorCheckIn: string | null;

}

const initialState: RideBookingCheckInState = {
    rideBooking: null,
    loadingFetch: true,
    errorFetch: null,
    loadingCheckIn: false,
    errorCheckIn: null,

};

export const fetchRideBookingCheckInDetails = createAsyncThunk<RideBookingCheckIn, string, { rejectValue: string }>(
    'rides/fetchRideBookingCheckInDetails',
    async (id, { rejectWithValue }) => {

        return rideBookingRepository.getBookingCheckInDetailsForDriver(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch ride booking details.');
            });
    }
);

export const checkInPassenger = createAsyncThunk<RideBookingCheckIn, string, { rejectValue: string }>(
    'rides/checkInPassenger',
    async (id, { rejectWithValue }) => {
        return rideBookingRepository.checkInPassenger(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to check-in passenger.');
            });
    }
);

const rideBookingCheckInSlice = createSlice({
    name: 'rideBookingCheckIn',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchRideBookingCheckInDetails.pending, () => {})
            .addCase(fetchRideBookingCheckInDetails.fulfilled, (state, action: PayloadAction<RideBookingCheckIn>) => {
                state.loadingFetch = false;
                state.rideBooking = action.payload;
            })
            .addCase(fetchRideBookingCheckInDetails.rejected, (state, action) => {
                state.loadingFetch = false;
                state.errorFetch = action.payload || 'Failed to fetch ride booking details. Please try again.';
            })

            .addCase(checkInPassenger.pending, (state) => {
                state.loadingCheckIn = true;
            })
            .addCase(checkInPassenger.fulfilled, (state, action: PayloadAction<RideBookingCheckIn>) => {
                state.loadingCheckIn = false;
            })
            .addCase(checkInPassenger.rejected, (state, action) => {
                state.loadingCheckIn = false;
                state.errorCheckIn = action.payload || 'Failed to check-in passenger. Please try again.';
            })

    },
});

export default rideBookingCheckInSlice.reducer;
