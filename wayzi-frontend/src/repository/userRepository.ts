import axiosInstance from "../axios/axiosInstance.ts";

const userRepository = {
    downloadProfilePic: (userId: string) => axiosInstance.get(`/user/${userId}/download-profile-pic`, {
        responseType: 'blob'
    }),
    submitProfilePic:  (data) => axiosInstance.post("/user/submit-profile-pic", data, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }}),
};

export default userRepository;