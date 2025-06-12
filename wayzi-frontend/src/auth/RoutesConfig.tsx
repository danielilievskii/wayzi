import {Route, Routes} from 'react-router-dom'
import Roles from "../enumerations/Roles.ts";
import {useEffect, useState} from "react";
import SignInPage from "../ui/pages/SignInPage/SignInPage.tsx";
import {HomePage} from "../ui/pages/HomePage/HomePage.tsx";
import {ErrorPage} from "../ui/pages/ErrorPage/ErrorPage.tsx";
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
import {useUser} from "../context/UserContext.tsx";
import {ProtectedRoute} from "../ui/components/auth/ProtectedRoute/ProtectedRoute.tsx";
import SignUpPage from "../ui/pages/SignUpPage/SignUpPage.tsx";
import {EmailSentPage} from "../ui/pages/EmailSentPage/EmailSentPage.tsx";
import {EmailVerificationPage} from "../ui/pages/EmailVerificationPage/EmailVerificationPage.tsx";
import {EditRideForm} from "../ui/pages/FormPages/EditRideForm.tsx";
import {RideBookersListPage} from "../ui/pages/RideBookersListPage/RideBookersListPage.tsx";

export const AllRoutes = [
    {
        component: HomePage,
        path: "/",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: SignInPage,
        path: '/login',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: SignUpPage,
        path: '/register',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: EmailSentPage,
        path: '/email-sent',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: EmailVerificationPage,
        path: '/verify-email',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: RidesPage,
        path: '/rides',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: RideDetailsPage,
        path: '/rides/:rideId',
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
            Roles.GUEST
        ],
        isProtected: false,
    },
    {
        component: PublishRideForm,
        path: "/rides/publish",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: PublishedRidesPage,
        path: "/rides/published",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: RideBookersListPage,
        path: "/rides/published/:rideId",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: EditRideForm,
        path: "/rides/published/:rideId/edit",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: BookRideForm,
        path: "/rides/:ride_id/book",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: RideBookingsPage,
        path: "/rides/bookings",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: RideBookingDetailsPage,
        path: "/rides/bookings/:rideBookingId",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: RideBookingCheckInPage,
        path: "/rides/bookings/:rideBookingId/check-in",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: ProfilePage,
        path: "/profile",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: AddVehicleForm,
        path: "/profile/vehicles/add",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
    {
        component: EditVehicleForm,
        path: "/profile/vehicles/edit/:vehicle_id",
        permission: [
            Roles.ADMIN_USER,
            Roles.STANDARD_USER,
        ],
        isProtected: true,
    },
]


const RoutesConfig = () => {
    const {loading} = useUser()

    if (loading) return <div>Loading...</div>;

    return (
        <Routes>
            {AllRoutes.map((route) => {
                const Component = route.component;

                return (
                    <Route
                        key={route.path}
                        path={route.path}
                        element={!route.isProtected ? <Component /> : <ProtectedRoute element={<Component />} />}
                    />
                );

            })}
            <Route path="*" element={<ErrorPage to="/" />} />
        </Routes>
    )
}

export default RoutesConfig