import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {downloadProfilePic} from "../redux/slices/profilePicSlice.ts";
import {useDispatch} from "react-redux";
import {AppDispatch} from "../redux/store.ts";
import authRepository from "../repository/authRepository.ts";
import {useNavigate} from "react-router";
import {SignUpSchemaType} from "../schemas/signUpSchema.ts";

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
    signIn: (data: any) => any;
    signUp: (data: SignUpSchemaType) => any;
    signOut: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export const UserProvider = ({children}: { children: ReactNode }) => {
    const navigate = useNavigate();

    const [currentUser, setCurrentUser] = useState<User | null>(null);
    const [loading, setLoading] = useState<boolean>(true);
    const dispatch = useDispatch<AppDispatch>();

    const signUp = (data: SignUpSchemaType) => {
        return authRepository.signUp(data)
            .then(() => {})
            .catch((error) => {
                console.log(error);

                return {
                    error: error?.response?.data || "An unexpected error occurred."
                };
            });
    };

    const signIn = (data: any) => {
        return authRepository.signIn(data)
            .then((response) => {
                setCurrentUser(response.data);
            })
            .catch((error) => {
                console.log(error);

                return {
                    error: error?.response?.data || "Invalid credentials. Please try again."
                };
            });
    };

    const signOut = () => {
        return authRepository.signOut()
            .then(() => {
                window.location.reload();
                // window.location.href = "/";
            })
            .catch((error) => {
                console.log(error);
            })
    };

    useEffect(() => {
        authRepository.me()
            .then(response => {
                setCurrentUser(response.data);
            })
            .catch(error => {
                console.log(error);
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    useEffect(() => {
        if (currentUser?.id) {
            dispatch(downloadProfilePic(currentUser.id));
        }
    }, [currentUser, dispatch]);

    return (
        <UserContext.Provider value={{currentUser, setCurrentUser, loading, signIn, signUp, signOut}}>
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