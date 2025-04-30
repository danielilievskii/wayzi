import axiosInstance from "../axios/axiosInstance.ts";


const API_URL = "http://localhost:8080/api/test/";

const getPublicContent = () => {
    return axiosInstance.get(API_URL + "all");
};

const getUserBoard = () => {
    return axiosInstance.get(API_URL + "user");
};

const getModeratorBoard = () => {
    return axiosInstance.get(API_URL + "mod");
};

const getAdminBoard = () => {
    return axiosInstance.get(API_URL + "admin");
};

export default {
    getPublicContent,
    getUserBoard,
    getModeratorBoard,
    getAdminBoard,
};