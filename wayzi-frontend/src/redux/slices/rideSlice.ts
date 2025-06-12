import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {Vehicle} from "./vehicleSlice";
import {Location} from "./locationSlice";
import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {FilterSchemaType} from "../../schemas/filterSchema.ts";
import {getLocalISOString} from "../../utils/dateUtils.ts";
import rideRepository from "../../repository/rideRepository.ts";
import {PublishRideSchemaType} from "../../schemas/publishRideSchema.ts";
import {EditRideSchemaType} from "../../schemas/editRideSchema.ts";


export interface RideStop {
    id: string;
    location: Location;
    stopAddress: string;
    stopTime: string;
    stopOrder: number;
}

export interface Ride {
    id: string;
    driverName: string
    driverId: string
    departureLocation: Location;
    departureAddress: string;
    departureTime: string;
    arrivalLocation: Location;
    arrivalAddress: string;
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

    createRideLoading: boolean;
    createRideError: string | null;

    updateRideLoading: boolean;
    updateRideError: string | null;

    updateRideStatusLoading: boolean;
    updateRideStatusError: string | null;
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

    loading: true,
    error: null,

    createRideLoading: false,
    createRideError: null,

    updateRideLoading: false,
    updateRideError: null,

    updateRideStatusLoading: false,
    updateRideStatusError: null,
};

export interface RidesResponse {
    rides: Ride[];
    totalPages: number;
    totalItems: number;
    currentPage: number;
}

export const fetchRides = createAsyncThunk<RidesResponse, any, { rejectValue: string }>(
    'rides/fetchFiltered',
    async (filterData, {rejectWithValue}) => {
        
        return rideRepository.findPage(filterData)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch rides.');
            })
    }
);

export const createRide = createAsyncThunk<Ride, PublishRideSchemaType, { rejectValue: string }>(
    'rides/publish',
    async (rideData, { rejectWithValue }) => {
        return rideRepository.createRide(rideData)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to create ride.');
            })
    }
);


export const updateRide = createAsyncThunk<Ride, { id: string; data: EditRideSchemaType }, { rejectValue: string }>(
    'rides/edit',
    async ({ id, data }, { rejectWithValue }) => {
        console.log("TUKA")
        console.log(id)

        return rideRepository.updateRide(id, data)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to update ride.');
            })
    }
);

export const updateRideStatus = createAsyncThunk<Ride, Partial<Ride>, { rejectValue: string }>(
    'rides/update-status',
    async (statusData, { rejectWithValue }) => {
        return rideRepository.updateRideStatus(statusData)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to update ride status.');
            })
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
        clearCreateRideError(state) {
            state.createRideError = null;
        },
        clearEditRideError(state) {
            state.updateRideError = null;
        },
    },
    extraReducers: (builder) => {
        builder
            // FETCH RIDES
            .addCase(fetchRides.pending, () => {})
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

            // CREATE RIDE
            .addCase(createRide.pending, (state) => {
                state.createRideLoading = true;
            })
            .addCase(createRide.fulfilled, (state) => {
                state.createRideLoading = false;
            })
            .addCase(createRide.rejected, (state, action) => {
                state.createRideLoading = false;
                state.createRideError = action.payload || "Something went wrong.";
            })

            // UPDATE RIDE
            .addCase(updateRide.pending, (state) => {
                state.updateRideLoading = true;
            })
            .addCase(updateRide.fulfilled, (state) => {
                state.updateRideLoading = false;
            })
            .addCase(updateRide.rejected, (state, action) => {
                state.createRideLoading = false;
                state.createRideError = action.payload || "Something went wrong.";
            })

            // UPDATE RIDE STATUS
            .addCase(updateRideStatus.pending, (state) => {
                state.updateRideStatusLoading = true;
            })
            .addCase(updateRideStatus.fulfilled, (state) => {
                state.updateRideStatusLoading = false;
            })
            .addCase(updateRideStatus.rejected, (state, action) => {
                state.updateRideStatusLoading = false;
                state.updateRideStatusError = action.payload || "Something went wrong.";
            })

    },
});

export const {setFilter, setPagination, clearCreateRideError, clearEditRideError} = rideSlice.actions;
export default rideSlice.reducer;
