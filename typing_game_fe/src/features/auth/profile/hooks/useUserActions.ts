"use client";

import { useState, useEffect } from "react";
export const useUserActions = () => {
    const [authHeader, setAuthHeader] = useState<string | null>(null);

    useEffect(() => {
        setAuthHeader(sessionStorage.getItem("authHeader"));
    }, []);
    //회원정보 수정
    const updateProfile = async (userId: string, username: string) => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        if(!authHeader) throw new Error("Not authHeader");

        await fetch(`${baseUrl}/user/${userId}`, {
            method :"PUT",
            headers: {
                "Content-Type": "application/json",
                Authorization: authHeader || "",
            },
            credentials: "include",
            body: JSON.stringify({ username })
        });
    };
    //이미지 업로드
    const updateProfileImg = async (file: File | null) => {
        if (!authHeader) return;
        if (!file) {
            alert("파일을 선택해주세요.");
            return;
        }
        const formData = new FormData();
        formData.append("file", file);
        try {   
            const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    
            await fetch(`${baseUrl}/user/profile`, {
                method: "POST",
                body: formData,
                headers: { Authorization: authHeader }, 
                credentials: "include",
            });
        } catch (err) {
            alert("업로드 실패");
        }
    };
    // 회원 탈퇴
    const deleteProfile = async () => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        if(!authHeader) throw new Error("Not authHeader");

        await fetch(`${baseUrl}/user`, {
            method :"DELETE",
            headers: {Authorization: authHeader || "",},
            credentials: "include",
        });
    };
    return {updateProfile, updateProfileImg, deleteProfile}
}