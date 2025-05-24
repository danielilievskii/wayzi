import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";
import {Location} from "./locationSlice.ts";
import {BookRideSchemaType} from "../../schemas/bookRideSchema.ts";
import rideBookingRepository from "../../repository/rideBookingRepository.ts";



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

    createRideBookingLoading: boolean;
    createRideBookingError: string | null;

    cancelRideBookingLoading: boolean;
    cancelRideBookingError: string | null;
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

    createRideBookingLoading: false,
    createRideBookingError: null,

    cancelRideBookingLoading: false,
    cancelRideBookingError: null,
}


export interface RideBookingsResponse {
    rideBookings: RideBooking[];
    totalPages: number;
    totalItems: number;
    currentPage: number;
}

export const fetchRideBookings = createAsyncThunk<RideBookingsResponse, {}, {rejectValue: string}> (
    'rideBookings/fetchAll',
    async (filterData, {rejectWithValue}) => {
        return rideBookingRepository.findPage(filterData)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch published rides.');
            })
    }
)

export const createRideBooking = createAsyncThunk<RideBooking, { id: string; data: BookRideSchemaType }, { rejectValue: string }>(
    'rideBookings/createRideBooking',
    async ({id, data}, { rejectWithValue }) => {
        return rideBookingRepository.createRideBooking(id, data)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to create ride booking.');
            })
    }
);

export const cancelRideBooking = createAsyncThunk<void, string, { rejectValue: string }>(
    'rideBookings/cancelRideBooking',
    async (id, { rejectWithValue }) => {

        return rideBookingRepository.cancelRideBooking(id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to cancel ride booking.');
            })
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
                state.createRideBookingLoading = true;
            })
            .addCase(createRideBooking.fulfilled, (state) => {
                state.createRideBookingLoading = false;
            })
            .addCase(createRideBooking.rejected, (state, action) => {
                state.createRideBookingLoading = false;
                state.createRideBookingError = action.payload || 'Something went wrong';
            })

            .addCase(cancelRideBooking.pending, (state) => {
                state.cancelRideBookingLoading = true;
            })
            .addCase(cancelRideBooking.fulfilled, (state) => {
                state.cancelRideBookingLoading = false;
            })
            .addCase(cancelRideBooking.rejected, (state, action) => {
                state.cancelRideBookingLoading = false;
                state.cancelRideBookingError = action.payload || 'Something went wrong';
            })

    }

})

export default rideBookingSlice.reducer;
export const { setFilter, setPagination } = rideBookingSlice.actions;
