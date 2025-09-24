"use client";

import { useState, useEffect } from "react";
import { splitByLength } from "../utils/splitByLength";

export const useTexts = (longTextId: number, isUserFile: boolean) => {
  console.log(longTextId, isUserFile)
  const [lyrics, setLyrics] = useState<string[]>([]);
    useEffect(() => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        const url = isUserFile
        ? `${baseUrl}/my-long-text/${longTextId}`
        : `${baseUrl}/long-text/${longTextId}`;
        fetch(url, {
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