import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";

export interface Vehicle {
    id: number;
    brand: string;
    model: string;
    color: string;
    type: string;
    capacity: number;
}

interface VehicleState {
    vehicles: Vehicle[];
    loading: boolean;
    error: string | null;
}

const initialState: VehicleState = {
    vehicles: [],
    loading: false,
    error: null,
};


export const fetchVehicles = createAsyncThunk(
    'profile/fetchAll',
    async (_, {rejectWithValue}) => {
        try {
            const res = await axiosInstance.get('/vehicle/all');
            return res.data as Vehicle[];
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to fetch vehicles');
        }
    }
)

// Async thunk to create a profile
export const createVehicle = createAsyncThunk(
    'profile/create',
    async (vehicleData: Omit<Vehicle, 'id'>, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.post('/vehicle/add', vehicleData);
            return res.data as Vehicle;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to create vehicle');
        }
    }
);

// Async thunk to edit a profile
export const editVehicle = createAsyncThunk(
    'profile/edit',
    async (
        { id, data }: { id: number; data: Partial<Omit<Vehicle, 'id'>> },
        { rejectWithValue }
    ) => {
        try {
            const res = await axiosInstance.post(`/vehicle/edit/${id}`, data);
            return res.data as Vehicle;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to update vehicle');
        }
    }
);

// Async thunk to delete a profile
export const deleteVehicle = createAsyncThunk(
    'profile/delete',
    async (id: number, { rejectWithValue }) => {
        try {
            await axiosInstance.delete(`/vehicle/delete/${id}`);
            return id;
        } catch (err: any) {
            return rejectWithValue(err.response?.data?.message || 'Failed to delete vehicle');
        }
    }
);

const vehicleSlice = createSlice({
    name: 'vehicle',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            //FETCH VEHICLES
            .addCase(fetchVehicles.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchVehicles.fulfilled, (state, action: PayloadAction<Vehicle[]>) => {
                state.loading = false;
                state.vehicles = action.payload;
            })
            .addCase(fetchVehicles.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // CREATE VEHICLE
            .addCase(createVehicle.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(createVehicle.fulfilled, (state, action: PayloadAction<Vehicle>) => {
                state.loading = false;
                state.vehicles.push(action.payload);
            })
            .addCase(createVehicle.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // EDIT VEHICLE
            .addCase(editVehicle.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(editVehicle.fulfilled, (state, action: PayloadAction<Vehicle>) => {
                state.loading = false;
                const index = state.vehicles.findIndex(v => v.id === action.payload.id);
                if (index !== -1) {
                    state.vehicles[index] = action.payload;
                }
            })
            .addCase(editVehicle.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            // DELETE VEHICLE
            .addCase(deleteVehicle.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(deleteVehicle.fulfilled, (state, action: PayloadAction<number>) => {
                state.loading = false;
                state.vehicles = state.vehicles.filter(v => v.id !== action.payload);
            })
            .addCase(deleteVehicle.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            });
    }
});

export default vehicleSlice.reducer;
