import {vehicleTypes} from "../constants/vehicleTypes.ts";

export const getVehicleIcon = (type: string) => {
    const vehicleType = vehicleTypes.find((item) => item.name === type);
    return vehicleType ? vehicleType.icon : '';
};