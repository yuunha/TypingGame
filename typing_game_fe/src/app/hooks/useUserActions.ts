import axios from "axios";

export const useUserActions = () => {
    //회원정보 수정
    const updateProfile = async (userId: string, username: string) => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;

        const authHeader = sessionStorage.getItem("authHeader");
        if(!authHeader) throw new Error("Not authHeader");

        return axios.put(`${baseUrl}/${userId}`, 
            { username },
            { withCredentials: true, headers: { Authorization: authHeader },}
        );
    };
    // 회원 탈퇴
    const deleteProfile = async () => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;

        const authHeader = sessionStorage.getItem("authHeader");
        if(!authHeader) throw new Error("Not authHeader");

        return axios.delete(`${baseUrl}/user`, 
            { withCredentials: true, headers: { Authorization: authHeader, },}
        );
    };
    return {updateProfile, deleteProfile}
}