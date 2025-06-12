import axiosInstance from "../axios/axiosInstance.ts";

const rideRepository = {
    findPage:  (data: any) => axiosInstance.get(`/rides`, {params: data}),
    findById:  (id: string) => axiosInstance.get(`/rides/${id}`),
    findRouteCoordinatesById:  (id: string) => axiosInstance.get(`/rides/${id}/route`),
    findPublishedRidesPage:  (data: any) => axiosInstance.get(`/rides/published`, {params: data}),

    createRide:  (data: any) => axiosInstance.post(`/rides`, data),
    updateRide:  (id: string, data: any) => axiosInstance.put(`/rides/published/${id}/edit`, data),
    updateRideStatus:  (data: any) => axiosInstance.put(`/rides/update-status`, data),

};

export default rideRepository;