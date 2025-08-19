"use client";
import { useState, useEffect } from "react";
import axios from "axios";

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

// 긴글 불러오기
// 로그인 - 비로그인 구분
export const useLongTexts = () => {
  const [lyricsList, setLyricsList] = useState<LongText[]>([]);

  useEffect(() => {

    const fetchLyrics = async () => {
      const baseUrl = process.env.NEXT_PUBLIC_API_URL;
      const authHeader = sessionStorage.getItem("authHeader");

      try {
        if(authHeader){
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
        } else {
          const allRes = await axios.get(`${baseUrl}/long-text`);
          const allLyrics: LongText[] = allRes.data.data.map((item: AllLyricsItem) => ({
            longTextId: item.longTextId,
            title: item.title,
            isUserFile: false,
          }));

          setLyricsList(allLyrics);
        }
        
      } catch (err) {
        console.error("긴글 불러오기 실패", err);
      }
    };

    fetchLyrics();
  }, []);

  return lyricsList;
};
