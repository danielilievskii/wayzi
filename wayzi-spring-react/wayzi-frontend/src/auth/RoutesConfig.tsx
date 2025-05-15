import {Navigate, Route, Router, Routes} from 'react-router-dom'

import Roles from "../enumerations/Roles.ts";
import {useSelector} from "react-redux";
import {useEffect, useState} from "react";
import SignInPage from "../ui/pages/SignInPage/SignInPage.tsx";
import {HomePage} from "../ui/pages/HomePage/HomePage.tsx";
import {RideBookingsPage} from "../ui/pages/RideBookingsPage/RideBookingsPage.tsx";
import {PublishedRidesPage} from "../ui/pages/PublishedRidesPage/PublishedRidesPage.tsx";
import {RidesPage} from "../ui/pages/RidesPage/RidesPage.tsx";
import {ProfilePage} from "../ui/pages/ProfilePage/ProfilePage.tsx";
import {AddVehicleForm} from "../ui/pages/FormPages/AddVehicleForm.tsx";
import {EditVehicleForm} from "../ui/pages/FormPages/EditVehicleForm.tsx";
import {RideDetailsPage} from "../ui/pages/RideDetailsPage/RideDetailsPage.tsx";
import {PublishRideForm} from "../ui/pages/FormPages/PublishRideForm.tsx";
import {BookRideForm} from "../ui/pages/FormPages/BookRideForm.tsx";
import {RideBookingDetailsPage} from "../ui/pages/RideBookingDetailsPage/RideBookingDetailsPage.tsx";
import {RideBookingCheckInPage} from "../ui/pages/RideBookingCheckInPage/RideBookingCheckInPage.tsx";

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
            <Route path="/rides/:rideId" element={<RideDetailsPage/>}></Route>
            <Route path="/rides/:ride_id/book" element={<BookRideForm/>}></Route>


            <Route path="/rides/bookings" element={<RideBookingsPage/>}></Route>
            <Route path="/rides/bookings/:rideBookingId" element={<RideBookingDetailsPage/>}></Route>
            <Route path="/rides/bookings/:rideBookingId/check-in" element={<RideBookingCheckInPage/>}></Route>



            <Route path="/rides/published" element={<PublishedRidesPage/>}></Route>
            <Route path="/rides/publish" element={<PublishRideForm/>}></Route>

            <Route path="/vehicles/add" element={<AddVehicleForm/>}></Route>
            <Route path="/vehicles/edit/:vehicle_id" element={<EditVehicleForm/>}></Route>
        </Routes>
    )
}

export default RoutesConfig