import { useEffect, useState } from "react";
import { useLongTexts } from "@/app/hooks/useLongTexts";

export const useLongTextWithScore = () => {
  const lyricsList = useLongTexts();
  const [longTextWithScore, setLongTextWithScore] = useState<any[]>([]);
  const authHeader = sessionStorage.getItem("authHeader");

  useEffect(() => {
    if (!lyricsList || lyricsList.length === 0) return;
    const fetchScore = async () => {
      const results = await Promise.all(
        lyricsList.map(async (item) => {
            const baseUrl = process.env.NEXT_PUBLIC_API_URL;
          const url = item.isUserFile
            ? `${baseUrl}/my-long-text/${item.longTextId}/score`
            : `${baseUrl}/long-text/${item.longTextId}/score`;
          try {
            const res = await fetch(url, {
              headers: { Authorization: authHeader || "" },
              credentials: "include",
            });
            const data = await res.json();
            return {
              ...item,
              score: data.score ?? null, // ✅ score 붙이기
            };
          } catch (err) {
            console.error("점수 조회 실패: ", err);
            return {
              ...item,
              score: null,
            };
          }
        })
      );
      setLongTextWithScore(results);
    };

    fetchScore();
  }, [lyricsList, authHeader]);

  return longTextWithScore; // ✅ 데이터만 리턴
};
