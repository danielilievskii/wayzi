import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import vehicleRepository from "../../repository/vehicleRepository.ts";
import {VehicleSchemaType} from "../../schemas/vehicleSchema.ts";

export interface Vehicle {
    id: string;
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

    createVehicleLoading: boolean;
    createVehicleError: string | null;

    editVehicleLoading: boolean;
    editVehicleError: string | null;

    deleteVehicleLoading: boolean;
    deleteVehicleError: string | null;
}

const initialState: VehicleState = {
    vehicles: [],
    loading: true,
    error: null,

    createVehicleLoading: false,
    createVehicleError: null,

    editVehicleLoading: false,
    editVehicleError: null,

    deleteVehicleLoading: false,
    deleteVehicleError: null,
};


export const fetchVehicles = createAsyncThunk(
    'vehicles/fetchVehicles',
    async (_, {rejectWithValue}) => {
        return vehicleRepository.findAll()
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to fetch vehicles.');
            })

    }
)

export const createVehicle = createAsyncThunk(
    'vehicles/createVehicle',
    async (data: VehicleSchemaType, {rejectWithValue}) => {
        return vehicleRepository.add(data)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to create vehicle.');
            })
    }
);

export const editVehicle = createAsyncThunk(
    'vehicles/editVehicle',
    async (
        {id, data}: { id: string; data: VehicleSchemaType },
        {rejectWithValue}
    ) => {
        return vehicleRepository.edit(data, id)
            .then((response) => {
                return response.data;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to edit vehicle.');
            })
    }
);

export const deleteVehicle = createAsyncThunk(
    'vehicles/deleteVehicle',
    async (id: string, {rejectWithValue}) => {
        return vehicleRepository.delete(id)
            .then(() => {
                return id;
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to delete vehicle.');
            })
    }
);

const vehicleSlice = createSlice({
    name: 'vehicle',
    initialState,
    reducers: {
        clearCreateVehicleError(state) {
            state.createVehicleError = null;
        },
        clearEditVehicleError(state) {
            state.editVehicleError = null;
        },

    },
    extraReducers: (builder) => {
        builder
            //FETCH VEHICLES
            .addCase(fetchVehicles.pending, () => {})
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
                state.createVehicleLoading = true;
            })
            .addCase(createVehicle.fulfilled, (state) => {
                state.createVehicleLoading = false;
            })
            .addCase(createVehicle.rejected, (state, action) => {
                state.createVehicleLoading = false;
                state.createVehicleError = action.payload as string;
            })

            // EDIT VEHICLE
            .addCase(editVehicle.pending, (state) => {
                state.editVehicleLoading = true;
            })
            .addCase(editVehicle.fulfilled, (state) => {
                state.editVehicleLoading = false
            })
            .addCase(editVehicle.rejected, (state, action) => {
                state.editVehicleLoading = false;
                state.editVehicleError = action.payload as string;
            })

            // DELETE VEHICLE
            .addCase(deleteVehicle.pending, (state) => {
                state.deleteVehicleLoading = true;
            })
            .addCase(deleteVehicle.fulfilled, (state) => {
                state.deleteVehicleLoading = false;
            })
            .addCase(deleteVehicle.rejected, (state, action) => {
                state.deleteVehicleLoading = false;
                state.deleteVehicleError = action.payload as string;
            });
    }
});

export default vehicleSlice.reducer;
export const { clearEditVehicleError, clearCreateVehicleError} = vehicleSlice.actions;

