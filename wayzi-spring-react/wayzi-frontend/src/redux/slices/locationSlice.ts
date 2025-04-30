import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";

export interface Location {
    id: number;
    name: string;
    city: string;
    longitude: number;
    latitude: number;
    displayName: string;
}

interface LocationState {
    locations: Location[];
    loading: boolean;
    error: string | null;
}

const initialState: LocationState = {
    locations: [],
    loading: false,
    error: null,
};


export const fetchLocations = createAsyncThunk(
    'locations/fetchAll',
    async (_, {rejectWithValue}) => {
        try {
            const res = await axiosInstance.get('/locations');
            return res.data as Location[];
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to fetch locations');
        }
    }
)


const locationSlice = createSlice({
    name: 'location',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            //FETCH VEHICLES
            .addCase(fetchLocations.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchLocations.fulfilled, (state, action: PayloadAction<Location[]>) => {
                state.loading = false;
                state.locations = action.payload;
            })
            .addCase(fetchLocations.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })


    }
});

export default locationSlice.reducer;
