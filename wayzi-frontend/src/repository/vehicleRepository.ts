import axiosInstance from "../axios/axiosInstance.ts";
import {VehicleSchemaType} from "../schemas/vehicleSchema.ts";

const vehicleRepository = {
    findById: (id: string) => axiosInstance.get(`/vehicles/${id}`),
    findAll: () => axiosInstance.get("/vehicles"),
    add: (data: VehicleSchemaType) => axiosInstance.post("/vehicles/add", data),
    edit: (data: VehicleSchemaType, id: string) => axiosInstance.put(`/vehicles/edit/${id}`, data),
    delete: (id: string) => axiosInstance.delete(`/vehicles/delete/${id}`),
};

export default vehicleRepository;
