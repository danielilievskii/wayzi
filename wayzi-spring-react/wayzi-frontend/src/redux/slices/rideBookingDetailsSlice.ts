import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance";
import {RideBooking} from "./rideBookingSlice.ts";


export interface RideBookingDetailsState {
    rideBooking: RideBooking | null;
    loading: boolean;
    error: string | null;
}

const initialState: RideBookingDetailsState = {
    rideBooking: null,
    loading: false,
    error: null,
};

export const fetchRideBookingDetails = createAsyncThunk<RideBooking, void, { rejectValue: string }>(
    'rides/fetchRideBookingDetails',
    async (id, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.get('/rides/bookings/' + id);
            return res.data as RideBooking;
        } catch (err: any) {
            console.log(err)
            return rejectWithValue(err.response?.data || 'Failed to fetch ride details');
        }
    }
);

const rideBookingDetailsSlice = createSlice({
    name: 'rideBookingDetails',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchRideBookingDetails.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchRideBookingDetails.fulfilled, (state, action: PayloadAction<RideBooking>) => {
                state.loading = false;
                state.rideBooking = action.payload;
            })
            .addCase(fetchRideBookingDetails.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch ride boooking rides';
            })

    },
});

export default rideBookingDetailsSlice.reducer;
