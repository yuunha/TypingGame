"use client";
import { useState, useEffect } from "react";

export const useFriendActions = () => {
    const [authHeader, setAuthHeader] = useState<string | null>(null);

    useEffect(() => {
        setAuthHeader(sessionStorage.getItem("authHeader"));
    }, []);
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;

    const handleReceivedRequests = async (
        friendRequestId : number,
        action: "ACCEPT" | "DECLINE"
    ) => {
        if (!authHeader) return;
        try {
        const res = await fetch(`${baseUrl}/friend-requests/${friendRequestId}`, {
            method: "PATCH",
            headers: { Authorization: authHeader, "Content-Type": "application/json",},
            credentials: "include",
            body: JSON.stringify({ action }),
        });
        if (!res.ok) {
            throw new Error(`서버 오류: ${res.status}`);
        }
        } catch (err) {
            console.error("친구 요청 처리 실패", err);
        }
    };

  return {
    handleReceivedRequests,
  };
};