"use client";
import { useState, useEffect } from "react";
import { LongText } from "@/app/types/long-text";

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
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    const authHeader = sessionStorage.getItem("authHeader");

    //로그인 상태
    if(authHeader){
      Promise.all([
        fetch(`${baseUrl}/long-text`, {
          headers: { Authorization: authHeader },
          credentials: "include",
        })
        .then(res => {
          if (!res.ok) throw new Error(`긴글 불러오기 에러: ${res.status}`);
          return res.json();
        }),

        fetch(`${baseUrl}/my-long-text`, {
          headers: { Authorization: authHeader },
          credentials: "include",
        })
        .then(res => {
          if (!res.ok) throw new Error(`나의 긴글 불러오기 에러: ${res.status}`);
          return res.json();
        }),
      ])
      .then(([allRes, myRes]) => {
        const allLyrics: LongText[] = allRes.data.map((item: AllLyricsItem) => ({
          longTextId: item.longTextId,
          title: item.title,
          isUserFile: false,
        }));

        const myLyrics: LongText[] = myRes.map((item: MyLyricsItem) => ({
          longTextId: item.myLongTextId,
          title: item.title,
          isUserFile: true,
        }));
        setLyricsList([...allLyrics, ...myLyrics]);
      })
      .catch(error => {
        console.error("긴글 불러오기 실패:", error);
      });
    } else {
      fetch(`${baseUrl}/long-text`)
      .then(res => {
        if (!res.ok) throw new Error(`긴글 불러오기 에러: ${res.status}`);
        return res.json();
      })
      .then(data => {
        const allLyrics: LongText[] = data.data.map((item: AllLyricsItem) => ({
          longTextId: item.longTextId,
          title: item.title,
          isUserFile: false,
        }));
        setLyricsList(allLyrics);
      })
      .catch(error => {
        console.error("긴글 불러오기 실패:", error);
      });
    }
  }, []);
  return lyricsList;
};
