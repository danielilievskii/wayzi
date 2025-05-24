import axiosInstance from "../axios/axiosInstance.ts";

const authRepository = {
    signIn:  (data) => axiosInstance.post("/auth/signin", data),
    signUp: (data) => axiosInstance.post("/auth/signup", data),
    signOut: () => axiosInstance.post("/auth/signout"),
    me:  () =>  axiosInstance.get("/auth/me"),
    verifyEmail: (token: string) => axiosInstance.post("/auth/verify-email?token=" + token),

};

export default authRepository;