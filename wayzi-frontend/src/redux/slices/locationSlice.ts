import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import locationRepository from "../../repository/locationRepository.ts";

export interface Location {
    id: string;
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
    loading: true,
    error: null,
};


export const fetchLocations = createAsyncThunk(
    'locations/fetchAll',
    async (_, {rejectWithValue}) => {
        return locationRepository.findAll()
            .then((res) => {
                return res.data as Location[];
            })
            .catch((err) => {
                return rejectWithValue(err.response?.data || 'Failed to fetch locations');
            });
    }
)


const locationSlice = createSlice({
    name: 'location',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            //FETCH LOCATIONS
            .addCase(fetchLocations.pending, () => {})
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
