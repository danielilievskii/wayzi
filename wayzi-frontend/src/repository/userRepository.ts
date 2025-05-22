import axiosInstance from "../axios/axiosInstance.ts";

const userRepository = {
    signIn: async (data) => {
        return await axiosInstance.post("/auth/signin", data);
    },
    signUp: (data) => axiosInstance.post("/auth/signup", data),
    signOut: async () => {
        return await axiosInstance.get("/auth/signout");
    },
    me: async () => {
        return await axiosInstance.get("/auth/me");
    },
    verifyEmail: (token: string) => axiosInstance.post("/auth/verify-email?token=" + token),

};

export default userRepository;