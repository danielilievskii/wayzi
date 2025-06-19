import {Navigate, useLocation} from "react-router-dom";
import {useUser} from "../../../../context/UserContext.tsx";
import {JSX} from "react"; // adjust path

export const ProtectedRoute = ({ element }: { element: JSX.Element })  => {
    const { currentUser, loading } = useUser();
    const location = useLocation();

    if (!currentUser && !loading) {
        return <Navigate to="/login" replace state={{ from: location }}/>;
    }

    return element
};