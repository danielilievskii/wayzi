import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance";
import { Vehicle } from "./vehicleSlice";
import { Location } from "./locationSlice";
import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {FilterSchemaType} from "../../schemas/filterSchema.ts";
import {getLocalISOString} from "../../utils/dateUtils.ts";


export interface RideStop {
    id: number;
    location: Location;
    stopTime: string;
    stopOrder: number;
}

export interface Ride {
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
}

export interface RidesState {
    rides: Ride[];
    totalPages: number;
    totalItems: number;
    currentPage: number;

    filter: FilterSchemaType | null;
    pagination: PaginationSchemaType;

    loading: boolean;
    error: string | null;
}

const initialState: RidesState = {
    rides: [],
    totalPages: 0,
    totalItems: 0,
    currentPage: 0,

    filter: {
        departureLocationId: null,
        arrivalLocationId: null,
        date: getLocalISOString(new Date()),
        passengersNum: null,
        departureLocationName: "",
        arrivalLocationName: "",
    },
    pagination: {pageNum: 1, pageSize: 10},

    loading: false,
    error: null,
};

export interface RidesResponse {
    rides: Ride[];
    totalPages: number;
    totalItems: number;
    currentPage: number;
}

// Fetch all rides
export const fetchRides = createAsyncThunk<RidesResponse, void, { rejectValue: string }>(
    'rides/fetchFiltered',
    async (filterData, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.get('/rides', {
                params: filterData,
            });
            return res.data;
        } catch (err: any) {
            return rejectWithValue(err.response?.data || 'Failed to fetch rides');
        }
    }
);



const rideSlice = createSlice({
    name: 'rides',
    initialState,
    reducers: {
        setFilter: (state, action: PayloadAction<FilterSchemaType>) => {
            state.filter = action.payload;
        },
        setPagination: (state, action: PayloadAction<PaginationSchemaType>) => {
            state.pagination = action.payload;
        },
    },
    extraReducers: (builder) => {
        builder
            // FILTER RIDES
            .addCase(fetchRides.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchRides.fulfilled, (state, action: PayloadAction<RidesResponse>) => {
                state.loading = false;
                state.rides = action.payload.rides;
                state.totalItems = action.payload.totalItems;
                state.totalPages = action.payload.totalPages;
                state.currentPage = action.payload.currentPage;
            })
            .addCase(fetchRides.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch rides';
            })

    },
});

export const { setFilter, setPagination } = rideSlice.actions;
export default rideSlice.reducer;
