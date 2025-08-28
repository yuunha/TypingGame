import { useState, useEffect } from "react";

export const useTexts = (longTextId: number, isUserFile: boolean) => {
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
        .then(data => setLyrics((data.content || "").split("\n")))
        .catch(err => console.error("가사 불러오기 실패", err));
    }, [longTextId, isUserFile]);
    return lyrics;
};