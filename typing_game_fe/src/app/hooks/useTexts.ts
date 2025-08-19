import { useState, useEffect } from "react";
import axios from "axios";

export const useTexts = (longTextId: number, isUserFile: boolean) => {
  const [lyrics, setLyrics] = useState<string[]>([]);


    useEffect(() => {
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        const url = isUserFile
        ? `${baseUrl}/my-long-text/${longTextId}`
        : `${baseUrl}/long-text/${longTextId}`;
        axios.get(url, { withCredentials: true })
        .then(res => {
            const data = res.data;
            setLyrics((data.content || "").split("\n"));
        })
        .catch(err => console.error("가사 불러오기 실패", err));
    }, [longTextId, isUserFile]);
    return lyrics;

};