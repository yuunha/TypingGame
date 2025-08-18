import axios from "axios";

export const useUserActions = () => {
    //회원정보 수정
    const updateProfile = async (userId: string, username: string) => {
        const authHeader = sessionStorage.getItem("authHeader");
        if(!authHeader) throw new Error("Not authHeader");

        return axios.put(`http://localhost:8080/user/${userId}`, 
            { username },
            { withCredentials: true, headers: { Authorization: authHeader },}
        );
    };
    // 회원 탈퇴
    const deleteProfile = async () => {
        const authHeader = sessionStorage.getItem("authHeader");
        if(!authHeader) throw new Error("Not authHeader");

        return axios.delete("http://localhost:8080/user", 
            { withCredentials: true, headers: { Authorization: authHeader, },}
        );
    };
    return {updateProfile, deleteProfile}
}