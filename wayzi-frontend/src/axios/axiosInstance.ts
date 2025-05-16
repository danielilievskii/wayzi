import axios from "axios";
import {BACKEND_URL} from "../config/config.ts";

const axiosInstance = axios.create({
    baseURL: BACKEND_URL + '/api',
    withCredentials: true,
});

export default axiosInstance;