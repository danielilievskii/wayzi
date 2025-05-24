import {createAsyncThunk, createSlice, PayloadAction} from "@reduxjs/toolkit";
import userRepository from "../../repository/userRepository.ts";

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
    async (userId, {rejectWithValue}) => {

        return userRepository.downloadProfilePic(userId)
            .then((response) => {
                const blob = new Blob([response.data]);
                const url = window.URL.createObjectURL(blob);
                return {userId, url};
            })
            .catch((error) => {
                const message =
                    typeof error.response?.data === 'string'
                        ? error.response.data
                        : error.response?.data?.message || 'Failed to download profile picture';
                return rejectWithValue(message);
            });
    }
);

export const submitProfilePic = createAsyncThunk<
    { userId: string; },                      // Return type
    { profilePicFile: File, userId: string }, // Arg type
    { rejectValue: string }
>(
    'profilePic/submit',
    async ({profilePicFile, userId}, {rejectWithValue}) => {
        const formData = new FormData();
        formData.append("file", profilePicFile);

        return userRepository.submitProfilePic(formData)
            .then(() => {
                return {userId}
            })
            .catch((error) => {
                return rejectWithValue(error.response?.data || 'Failed to submit profile picture');
            });
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