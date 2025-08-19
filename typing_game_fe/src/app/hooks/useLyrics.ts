"use client";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "./useAuth";

export interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
}

interface AllLyricsItem {
  longTextId: number;
  title: string;
}

interface MyLyricsItem {
  myLongTextId: number;
  title: string;
}


export const useLyrics = () => {
  const { isLoggedIn } = useAuth();
  const [lyricsList, setLyricsList] = useState<LongText[]>([]);

  useEffect(() => {
    if (!isLoggedIn) return;

    const fetchLyrics = async () => {
      const baseUrl = process.env.NEXT_PUBLIC_API_URL;
      const authHeader = sessionStorage.getItem("authHeader");
      if (!authHeader) return;

      try {
        const [allRes, myRes] = await Promise.all([
          axios.get(`${baseUrl}/long-text`),
          axios.get(`${baseUrl}/my-long-text`, {
            headers: { Authorization: authHeader },
            withCredentials: true,
          }),
        ]);
        
        // any XX
        const allLyrics: LongText[] = allRes.data.data.map((item: AllLyricsItem) => ({
          longTextId: item.longTextId,
          title: item.title,
          isUserFile: false,
        }));

        const myLyrics: LongText[] = myRes.data.map((item: MyLyricsItem) => ({
          longTextId: item.myLongTextId,
          title: item.title,
          isUserFile: true,
        }));

        setLyricsList([...allLyrics, ...myLyrics]);
      } catch (err) {
        console.error("긴글 불러오기 실패", err);
      }
    };

    fetchLyrics();
  }, [isLoggedIn]);

  return lyricsList;
};
