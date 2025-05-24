import {Ride, RidesResponse} from "./rideSlice.ts";
import {PaginationSchemaType} from "../../schemas/paginationSchema.ts";
import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {FilterSchemaType} from "../../schemas/filterSchema.ts";
import rideRepository from "../../repository/rideRepository.ts";


interface PublishedRidesStates {
    publishedRides: Ride[];
    totalPages: number;
    totalItems: number;
    currentPage: number;

    filter: { status: string | null };
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
        status: null,
    },

    pagination: {pageNum: 1, pageSize: 10},

    loading: true,
    error: null,
}

export const fetchPublishedRides = createAsyncThunk<RidesResponse, {}, { rejectValue: string }>(
    'ridesPublished/fetchAll',
    async (filterData, {rejectWithValue}) => {
        return rideRepository.findPublishedRidesPage(filterData)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch published rides.');
            })
    }
)


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
            .addCase(fetchPublishedRides.pending, () => {
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
    }

})

export const {setFilter, setPagination} = publishedRideSlice.actions;
export default publishedRideSlice.reducer;