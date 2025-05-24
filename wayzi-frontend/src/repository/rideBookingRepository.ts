import axiosInstance from "../axios/axiosInstance.ts";
import {BookRideSchemaType} from "../schemas/bookRideSchema.ts";

const rideBookingRepository = {
    findPage:  (data: any) => axiosInstance.get(`/rides/bookings`, {params: data}),
    createRideBooking:  (rideId: string, data: BookRideSchemaType) => axiosInstance.post(`/rides/${rideId}/book`, data),
    cancelRideBooking:  (id: string) => axiosInstance.put(`/rides/bookings/${id}/cancel`),

    getBookingDetailsForBooker:  (id: string) => axiosInstance.get(`/rides/bookings/${id}`),

    getBookingCheckInDetailsForDriver:  (id: string) => axiosInstance.get(`/rides/bookings/${id}/check-in`),
    checkInPassenger:  (id: string) => axiosInstance.put(`/rides/bookings/${id}/check-in`),

};

export default rideBookingRepository;