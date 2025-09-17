"use client";
import { useState, useEffect } from "react";
import { Constitution } from '@/app/types/constitution'

export const useConstitution = () => {
    const [constitution, setConstitution] = useState<Constitution | null>(null);
    const authHeader = sessionStorage.getItem("authHeader");
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;

    const fetchConstitution = async (articleIndex : number) => {
        if (!authHeader) return;
        try {
        const res = await fetch(`${baseUrl}/constitution/${articleIndex}`, {
            headers: { Authorization: authHeader },
            credentials: "include",
        });
        const data = await res.json();
        setConstitution(data);
        } catch (err) {
        console.error("헌법 조회 실패", err);
        }
    };
    return {constitution, fetchConstitution};
};
