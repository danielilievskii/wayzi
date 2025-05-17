import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import axiosInstance from "../axios/axiosInstance.ts";
import {downloadProfilePic} from "../redux/slices/profilePicSlice.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../redux/store.ts";

interface User {
    id: number;
    email: string;
    name: string;
    role: string;
}

interface UserContextType {
    currentUser: User | null;
    setCurrentUser: (user: User | null) => void;
    loading: boolean;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
    const [currentUser, setCurrentUser] = useState<User | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axiosInstance.get("/auth/me");
                setCurrentUser(response.data);
            } catch (err) {
                console.log(err);
            }
            setLoading(false)
        }

        fetchUser();
    }, []);

    useEffect(() => {
        if (currentUser?.id) {
            dispatch(downloadProfilePic(currentUser.id));
        }
    }, [currentUser, dispatch]);

    return (
        <UserContext.Provider value={{ currentUser, setCurrentUser, loading }}>
            {children}
        </UserContext.Provider>
    );
};

// Optional
export const useUser = (): UserContextType => {
    const context = useContext(UserContext);
    if (!context) {
        throw new Error("useUser must be used within a UserProvider");
    }
    return context;
};