import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import axiosInstance from "../../axios/axiosInstance.ts";

interface ProfilePicState {
    pictures: Record<string, string>;
    loading: boolean;
    error: string | null;
}

const initialState: ProfilePicState = {
    pictures: {},
    loading: false,
    error: null,
};

export const downloadProfilePic = createAsyncThunk<
    { userId: string; url: string },
    string,
    { rejectValue: string }
>(
    'profilePic/download',
    async (userId, { rejectWithValue }) => {
        try {
            const res = await axiosInstance.get(`/user/${userId}/download-profile-pic`, {
                responseType: 'blob'
            });
            const blob = new Blob([res.data]);
            const url = window.URL.createObjectURL(blob);

            return { userId, url };
        } catch (err: any) {
            if (err.response?.data instanceof Blob) {
                return rejectWithValue('Profile picture could not be downloaded.');
            }
            return rejectWithValue(err.response?.data || 'Failed to download profile picture');
        }
    }
);

export const submitProfilePic = createAsyncThunk<
    { userId: string; },                      // Return type
    { profilePicFile: File, userId: string }, // Arg type
    { rejectValue: string }
>(
    'profilePic/submit',
    async ({ profilePicFile, userId }, { rejectWithValue }) => {
        try {
            const formData = new FormData();
            formData.append("file", profilePicFile);

            const response = await axiosInstance.post(`/user/submit-profile-pic`, formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });

            if (response.status === 200) {
                return { userId };
            } else {
                return rejectWithValue("Unexpected response status: " + response.status);
            }
        } catch (error: any) {
            return rejectWithValue(error.response?.data || 'Failed to submit profile picture');
        }
    }
);

const profilePicSlice = createSlice({
    name: 'profilePic',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(downloadProfilePic.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(downloadProfilePic.fulfilled, (state, action: PayloadAction<{ userId: string; url: string }>) => {
                state.loading = false;
                state.pictures[action.payload.userId] = action.payload.url;
            })
            .addCase(downloadProfilePic.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })

            .addCase(submitProfilePic.fulfilled, (state) => {
                state.loading = false;
            })
            .addCase(submitProfilePic.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(submitProfilePic.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload as string;
            })
    }
});

export default profilePicSlice.reducer;