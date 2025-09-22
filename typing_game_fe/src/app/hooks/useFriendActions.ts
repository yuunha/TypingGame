"use client";
import { useState, useEffect } from "react";

export const useFriendActions = () => {
    const [authHeader, setAuthHeader] = useState<string | null>(null);

    useEffect(() => {
        setAuthHeader(sessionStorage.getItem("authHeader"));
    }, []);
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;

    const rejectReceivedRequests = async (friendRequestId : number) => {
        if (!authHeader) return;
        try {
        const res = await fetch(`${baseUrl}/friend-requests/${friendRequestId}`, {
            method: "PATCH",
            headers: { Authorization: authHeader, "Content-Type": "application/json",},
            credentials: "include",
            body: JSON.stringify({ status: "REJECT" }),
        });
        if (!res.ok) {
            throw new Error(`서버 오류: ${res.status}`);
        }
        } catch (err) {
        console.error("친구 요청 거부 실패", err);
        }
    };

    const acceptReceivedRequests = async (friendRequestId : number) => {
        if (!authHeader) return;
        try {
        const res = await fetch(`${baseUrl}/friend-requests/${friendRequestId}`, {
            method: "PATCH",
            headers: { Authorization: authHeader, "Content-Type": "application/json",},
            credentials: "include",
            body: JSON.stringify({ status: "ACCEPT" }),
        });
        if (!res.ok) {
            throw new Error(`서버 오류: ${res.status}`);
        }
        } catch (err) {
        console.error("친구 요청 허용 실패", err);
        }
    };

  return {
    acceptReceivedRequests,
    rejectReceivedRequests,
  };
};