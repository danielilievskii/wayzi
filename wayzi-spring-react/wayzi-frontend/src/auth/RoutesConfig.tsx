import {Navigate, Route, Router, Routes} from 'react-router-dom'

import Roles from "../enumerations/Roles.ts";
import {useSelector} from "react-redux";
import {useEffect, useState} from "react";
import SignInPage from "../pages/auth/SignInPage.tsx";
import {HomePage} from "../pages/HomePage.tsx";
import {RidesBookedPage} from "../pages/rides/RidesBookedPage.tsx";
import {RidesPublishedPage} from "../pages/rides/RidesPublishedPage.tsx";
import {RidesPage} from "../pages/rides/RidesPage.tsx";
import {ProfilePage} from "../pages/ProfilePage.tsx";
import {AddVehicleForm} from "../pages/vehicles/AddVehicleForm.tsx";
import {EditVehicleForm} from "../pages/vehicles/EditVehicleForm.tsx";
import {RideDetails} from "../pages/rides/RideDetails.tsx";
import {PublishRideForm} from "../pages/rides/PublishRideForm.tsx";

export const PrivateRoutes = [


]

export const PublicRoutes = [
    {
        component: HomePage,
        path: "/",
        exact: true,
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ]
    },
    {
        component: SignInPage,
        path: '/signin',
        title: 'Sign In',
        exact: true,
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ]
    },

]

const AllRoutes = [...PrivateRoutes, ...PublicRoutes];

// const filterRoutes = (roleParam: string) => {
//     return AllRoutes.filter(route => {
//         return route.permission.includes(roleParam);
//     });
// };
const RoutesConfig = () => {
    // // const currentUser = useSelector(state => state.auth.currentUser);
    // // const [role, setRole] = useState(Roles.GUEST);
    //
    // useEffect(() => {
    //     if (currentUser) {
    //         setRole(currentUser.role);
    //     }
    // }, [currentUser]);

    return (
        // <Routes>
        //     {filterRoutes(role).map(route => (
        //         <Route
        //             key={route.path}
        //             path={route.path}
        //             element={<route.component/>}
        //             exact={route.exact}
        //         />
        //     ))}
        //     <Route path="*" element={<ErrorPage to="/"/>}/>
        // </Routes>

        <Routes>
            <Route path="/" element={<HomePage />}></Route>
            <Route path="/profile" element={<ProfilePage />}></Route>
            <Route path="/signin" element={<SignInPage/>}></Route>
            <Route path="/rides" element={<RidesPage/>}></Route>
            <Route path="/rides/details/:ride_id" element={<RideDetails/>}></Route>
            <Route path="/rides/booked" element={<RidesBookedPage/>}></Route>

            <Route path="/rides/published" element={<RidesPublishedPage/>}></Route>
            <Route path="/rides/publish" element={<PublishRideForm/>}></Route>

            <Route path="/vehicles/add" element={<AddVehicleForm/>}></Route>
            <Route path="/vehicles/edit/:vehicle_id" element={<EditVehicleForm/>}></Route>
        </Routes>
    )
}

export default RoutesConfig