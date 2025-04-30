import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import axiosInstance from "../axios/axiosInstance.ts";

interface User {
    id: number;
    email: string;
    name: string;
    role: string;
}

interface UserContextType {
    currentUser: User | null;
    setCurrentUser: (user: User | null) => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({ children }: { children: ReactNode }) => {
    const [currentUser, setCurrentUser] = useState<User | null>(null);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await axiosInstance.get("/auth/me");
                setCurrentUser(response.data);
            } catch (err) {
                console.log(err);
            }
        }

        fetchUser();
    }, []);

    return (
        <UserContext.Provider value={{ currentUser, setCurrentUser }}>
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