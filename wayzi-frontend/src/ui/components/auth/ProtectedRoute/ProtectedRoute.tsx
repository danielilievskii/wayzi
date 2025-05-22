import {Navigate, useLocation} from "react-router-dom";
import {useUser} from "../../../../context/UserContext.tsx";
import {JSX} from "react"; // adjust path

export const ProtectedRoute = ({ element }: { element: JSX.Element })  => {
    const { currentUser, loading } = useUser();
    const location = useLocation();

    console.log("currentUser:", currentUser);
    console.log("ProtectedRoute mounted for path:", location.pathname);

    if (!currentUser && !loading) {
        return <Navigate to="/login" replace state={{ from: location }}/>;
    }

    return element
};