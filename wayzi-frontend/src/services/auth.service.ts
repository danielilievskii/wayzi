import axiosInstance from "../axios/axiosInstance.ts";

const API_URL = "http://localhost:8080/api/auth/";

const register = (username: string, email: string, password: string) => {
    return axiosInstance.post(API_URL + "signup", {
        username,
        email,
        password,
    });
};

const login = (username: string, password: string) => {
    return axiosInstance
        .post(API_URL + "signin", {
            username,
            password,
        })
        .then((response) => {
            if (response.data.accessToken) {
                localStorage.setItem("user", JSON.stringify(response.data));
            }

            return response.data;
        });
};

const logout = () => {
    localStorage.removeItem("user");
};

export default {
    register,
    login,
    logout,
};