"use client";
import { useState, useEffect } from "react";
import axios from "axios";
import { useAuth } from "./useAuth";

export interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
}

export const useLyrics = () => {
  const { isLoggedIn } = useAuth();
  const [lyricsList, setLyricsList] = useState<LongText[]>([]);

  useEffect(() => {
    if (!isLoggedIn) return;

    const fetchLyrics = async () => {
      const authHeader = sessionStorage.getItem("authHeader");
      if (!authHeader) return;

      try {
        const [allRes, myRes] = await Promise.all([
          axios.get("http://localhost:8080/long-text"),
          axios.get("http://localhost:8080/my-long-text", {
            headers: { Authorization: authHeader },
            withCredentials: true,
          }),
        ]);

        const allLyrics: LongText[] = allRes.data.data.map((item: any) => ({
          longTextId: item.longTextId,
          title: item.title,
          isUserFile: false,
        }));

        const myLyrics: LongText[] = myRes.data.map((item: any) => ({
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
