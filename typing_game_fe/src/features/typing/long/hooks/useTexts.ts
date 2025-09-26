"use client";

import { useState, useEffect } from "react";
import { splitByLength } from "@/utils/splitByLength";

export const useTexts = (longTextId: number, isUserFile: boolean) => {
  const [lyrics, setLyrics] = useState<string[]>([]);
    useEffect(() => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        const url = isUserFile
        ? `${baseUrl}/my-long-text/${longTextId}`
        : `${baseUrl}/long-text/${longTextId}`;
        const authHeader = sessionStorage.getItem("authHeader");
        const headers: HeadersInit = {};
        if (isUserFile && authHeader) {
          headers["Authorization"] = authHeader;
        }
        fetch(url, {
          headers,
          credentials: "include",
        })
        .then(res => res.json())
        .then(data => {
          const lines = splitByLength(data.content,80);
          setLyrics((lines || ""))}
        )
        .catch(err => console.error("가사 불러오기 실패", err));
    }, [longTextId, isUserFile]);
    return lyrics;
};