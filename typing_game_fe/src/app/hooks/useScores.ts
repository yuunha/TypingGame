import { useEffect, useState } from "react";


interface ScoreItem {
    score : number;
}
  
// 특정 긴글의 점수목록 조회
export function useScores(longTextId:number, isUserFile:boolean){
    const [score, setScore] = useState(0);
    const [loading, setLoading] = useState(true);

    useEffect(()=>{
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;

        const url = isUserFile
            ? `${baseUrl}/my-long-text/${longTextId}/score`
            : `${baseUrl}/long-text/${longTextId}/score`;
        setLoading(true);

        fetch(url,{
            credentials: "include",
        })
        .then((res)=>res.json())
        .then((data)=> {
            const scores:ScoreItem[] = data.data;
            const maxScore = scores.reduce((max, cur) => Math.max(max,cur.score), 0);
            setScore(maxScore)
        })
        .finally(() => setLoading(false));

    }, [longTextId, isUserFile]);
    return {score, loading, setScore}
}