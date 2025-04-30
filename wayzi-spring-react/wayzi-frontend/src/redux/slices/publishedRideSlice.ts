import {Ride, RidesResponse} from "./rideSlice.ts";
import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";
import {FilterSchemaType} from "../../schemas/filterSchema.ts";


interface PublishedRidesStates {
    publishedRides: Ride[];
    totalPages: number;
    totalItems: number;
    currentPage: number;

    filter: FilterSchemaType | null;
    pagination: PaginationSchemaType;

    loading: boolean;
    error: string | null;
}

const initialState: PublishedRidesStates = {
    publishedRides: [],
    totalPages: 0,
    totalItems: 0,
    currentPage: 0,

    filter: {
        departureLocationId: null,
        arrivalLocationId: null,
        date: new Date().toISOString().split('T')[0],
        passengersNum: null,
        departureLocationName: "",
        arrivalLocationName: "",
    },

    pagination: {pageNum: 1, pageSize: 10},

    loading: false,
    error: null,
}

export const fetchPublishedRides = createAsyncThunk<RidesResponse, void, {rejectValue: string}> (
    'ridesPublished/fetchAll',
    async (filterData, {rejectWithValue}) => {
        try {
            const res = await axiosInstance.get('/rides/published', {
                params: filterData
            });
            console.log("TUKA")
            console.log(res.data)
            return res.data as RidesResponse;

        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to fetch published rides');
        }
    }
)

// Create a ride
export const updateRideStatus = createAsyncThunk<Ride, Partial<Ride>, { rejectValue: string }>(
    'rides/update-status',
    async (statusData, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.post('/rides/update-status', statusData);
            return res.data;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to update ride status');
        }
    }
);

// Create a ride
export const createRide = createAsyncThunk<Ride, Partial<Ride>, { rejectValue: string }>(
    'rides/create',
    async (rideData, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.post('/rides/add', rideData);
            return res.data;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to create ride');
        }
    }
);

// Edit a ride
export const editRide = createAsyncThunk<Ride, { id: number; data: Partial<Ride> }, { rejectValue: string }>(
    'rides/edit',
    async ({ id, data }, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.put(`/rides/edit/${id}`, data);
            return res.data;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to update ride');
        }
    }
);

// Delete a ride
export const deleteRide = createAsyncThunk<number, number, { rejectValue: string }>(
    'rides/delete',
    async (id, { rejectWithValue }) => {
        try {
            await axiosInstance.delete(`/rides/delete/${id}`);
            return id;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to delete ride');
        }
    }
);

const publishedRideSlice = createSlice({
    name: 'publishedRide',
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
            .addCase(fetchPublishedRides.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchPublishedRides.fulfilled, (state, action: PayloadAction<RidesResponse>) => {
                state.loading = false;
                state.publishedRides = action.payload.rides;
                state.totalItems = action.payload.totalItems;
                state.totalPages = action.payload.totalPages;
                state.currentPage = action.payload.currentPage;
            })
            .addCase(fetchPublishedRides.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Failed to fetch published rides';
            })

            // CREATE RIDE
            .addCase(createRide.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(createRide.fulfilled, (state, action: PayloadAction<Ride>) => {
                state.loading = false;
                state.publishedRides.push(action.payload);
            })
            .addCase(createRide.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })

            // EDIT RIDE
            .addCase(editRide.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(editRide.fulfilled, (state, action: PayloadAction<Ride>) => {
                state.loading = false;
                const index = state.publishedRides.findIndex(r => r.id === action.payload.id);
                if (index !== -1) {
                    state.publishedRides[index] = action.payload;
                }
            })
            .addCase(editRide.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })

            // DELETE RIDE
            .addCase(deleteRide.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(deleteRide.fulfilled, (state, action: PayloadAction<number>) => {
                state.loading = false;
                state.publishedRides = state.publishedRides.filter(r => r.id !== action.payload);
            })
            .addCase(deleteRide.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })

            // UPDATE RIDE STATUS
            .addCase(updateRideStatus.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(updateRideStatus.fulfilled, (state, action: PayloadAction<Ride>) => {
                state.loading = false;
                const index = state.publishedRides.findIndex(r => r.id === action.payload.id);
                if (index !== -1) {
                    state.publishedRides[index] = action.payload;
                }
            })
            .addCase(updateRideStatus.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Something went wrong';
            })
    }

})

export const { setFilter, setPagination } = publishedRideSlice.actions;
export default publishedRideSlice.reducer;