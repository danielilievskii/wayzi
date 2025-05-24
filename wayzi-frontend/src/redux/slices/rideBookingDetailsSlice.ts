import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import {RideBooking} from "./rideBookingSlice.ts";
import rideBookingRepository from "../../repository/rideBookingRepository.ts";


export interface RideBookingDetailsState {
    rideBooking: RideBooking | null;
    loading: boolean;
    error: string | null;
}

const initialState: RideBookingDetailsState = {
    rideBooking: null,
    loading: true,
    error: null,
};

export const fetchBookingDetailsForBooker = createAsyncThunk<RideBooking, string, { rejectValue: string }>(
    'rides/fetchBookingDetailsForBooker',
    async (id, { rejectWithValue }) => {

        return rideBookingRepository.getBookingDetailsForBooker(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch ride booking details.');
            })
    }
);

const rideBookingDetailsSlice = createSlice({
    name: 'rideBookingDetails',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchBookingDetailsForBooker.pending, () => {})
            .addCase(fetchBookingDetailsForBooker.fulfilled, (state, action: PayloadAction<RideBooking>) => {
                state.loading = false;
                state.rideBooking = action.payload;
            })
            .addCase(fetchBookingDetailsForBooker.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong.';
            })

    },
});

export default rideBookingDetailsSlice.reducer;
