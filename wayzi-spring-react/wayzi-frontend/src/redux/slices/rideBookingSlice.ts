import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";
import {Location} from "./locationSlice.ts";
import {BookRideSchemaType} from "../../schemas/bookRideSchema.ts";



export interface RideBooking {
    rideBookingId: string;
    rideId: string
    driverName: string;
    driverId: string;
    departureLocation: Location;
    departureTime: string;
    arrivalLocation: Location;
    arrivalTime: string;
    paymentMethod: string;
    rideBookingStatus: string;
    checkInStatus: string;
    bookedSeats: number;
    totalPrice: number;
    message: string;
    qrCodeUrl: string;
    bookingTime: string;
}

interface RideBookingsState {
    rideBookings: RideBooking[];
    totalPages: number;
    totalItems: number;
    currentPage: number;

    filter: {
        status: "CONFIRMED" | "ARCHIVED" | "CANCELED" | null;
    };

    pagination: PaginationSchemaType;

    loading: boolean;
    error: string | null;
}

const initialState: RideBookingsState = {
    rideBookings: [],
    totalPages: 0,
    totalItems: 0,
    currentPage: 0,

    filter: {
        status: null,
    },

    pagination: {pageNum: 1, pageSize: 10},

    loading: false,
    error: null,
}


export interface RideBookingsResponse {
    rideBookings: RideBooking[];
    totalPages: number;
    totalItems: number;
    currentPage: number;
}

export const fetchRideBooking = createAsyncThunk<RideBooking, void, {rejectValue: string}> (
    'rideBookings/fetchById',
    async (rideBookingId, {rejectWithValue}) => {
        try {
            const res = await axiosInstance.get('/rides/bookings/' + rideBookingId);
            return res.data as RideBooking;

        } catch (err: any) {
            return rejectWithValue(err.response?.data || 'Failed to fetch booked rides');
        }
    }
)

export const fetchRideBookings = createAsyncThunk<RideBookingsResponse, any, {rejectValue: string}> (
    'rideBookings/fetchAll',
    async (filterData, {rejectWithValue}) => {
        try {
            console.log(filterData)
            const res = await axiosInstance.get('/rides/bookings', {
                params: filterData
            });
            console.log(res.data)
            return res.data as RideBookingsResponse;

        } catch (err: any) {
            return rejectWithValue(err.response?.data || 'Failed to fetch booked rides');
        }
    }
)

export const cancelRideBooking = createAsyncThunk<void, number, { rejectValue: string }>(
    'rideBookings/cancelRideBooking',
    async (id, { rejectWithValue }) => {
        try {

            const res = await axiosInstance.post('/rides/bookings/' + id + '/cancel');
            return res.data;
        } catch (err: any) {
            return rejectWithValue(err.response?.data || 'Failed to cancel ride booking');
        }
    }
);

export const createRideBooking = createAsyncThunk<RideBooking, { id: number; data: BookRideSchemaType }, { rejectValue: string }>(
    'rideBookings/createRideBooking',
    async ({id, data}, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.post(`/rides/${id}/book`, data);
            return res.data;
        } catch (err: any) {
            console.log(err.response)
            return rejectWithValue(err.response?.data || 'Failed to create ride');
        }
    }
);



const rideBookingSlice = createSlice({
    name: 'rideBooking',
    initialState,
    reducers: {
        setFilter: (state, action: PayloadAction<any>) => {
            state.filter = action.payload;
        },
        setPagination: (state, action: PayloadAction<PaginationSchemaType>) => {
            state.pagination = action.payload;
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchRideBooking.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchRideBooking.fulfilled, (state, action: PayloadAction<RideBooking>) => {
                state.loading = false;
                state.rideBookings = {...state.rideBookings, ...action.payload};
            })
            .addCase(fetchRideBooking.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch ride boooking rides';
            })

            .addCase(fetchRideBookings.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchRideBookings.fulfilled, (state, action: PayloadAction<RideBookingsResponse>) => {
                state.loading = false;
                state.rideBookings = action.payload.rideBookings;
                state.totalItems = action.payload.totalItems;
                state.totalPages = action.payload.totalPages;
                state.currentPage = action.payload.currentPage;
            })
            .addCase(fetchRideBookings.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch published rides';
            })

            .addCase(createRideBooking.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(createRideBooking.fulfilled, (state, action: PayloadAction<RideBooking>) => {
                state.loading = false;
            })
            .addCase(createRideBooking.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })

            .addCase(cancelRideBooking.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(cancelRideBooking.fulfilled, (state) => {
                state.loading = false;
            })
            .addCase(cancelRideBooking.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })

    }

})

export default rideBookingSlice.reducer;
export const { setFilter, setPagination } = rideBookingSlice.actions;
