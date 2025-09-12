import { useEffect, useState } from "react";

  
// 특정 긴글의 점수목록 조회
export function useScores(longTextId:number, isUserFile:boolean){
    const authHeader = sessionStorage.getItem("authHeader");
    const [score, setScore] = useState(0);

    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    const url = isUserFile
        ? `${baseUrl}/my-long-text/${longTextId}/score`
        : `${baseUrl}/long-text/${longTextId}/score`;
    
    const fetchScore = async () => {
        if (!authHeader) return;
        try {
        const res = await fetch(`${url}`, {
            headers: { Authorization: authHeader },
            credentials: "include",
        });
        const data = await res.json();
        setScore(data.score);
        } catch (err) {
        console.error("최고 점수 조회 실패", err);
        }
    };
    return {score, fetchScore}
}