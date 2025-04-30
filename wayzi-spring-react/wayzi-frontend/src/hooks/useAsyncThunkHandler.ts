import {useState} from "react";
import {AppDispatch} from "../redux/store.ts";

export const useAsyncThunkHandler = () => {
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const handleThunk = async <T>(
        dispatchFn: AppDispatch,
        thunk: any,
        data: any,
        onSuccess?: (result: T) => void
    ) => {
        setLoading(true);
        setError(null);
        setSuccess(false);

        const resultAction = await dispatchFn(thunk(data));
        setLoading(false);

        if (thunk.fulfilled.match(resultAction)) {
            setSuccess(true);
            onSuccess?.(resultAction.payload);
        } else {
            setError(resultAction.payload as string || 'Something went wrong');
        }
    };

    return { handleThunk, loading, success, error };
};