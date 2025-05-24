import axiosInstance from "../axios/axiosInstance.ts";

const locationRepository = {
    findAll: () => axiosInstance.get("/locations"),
};

export default locationRepository;
