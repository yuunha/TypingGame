import axios, { AxiosError } from "axios";
import { useEffect, useState } from "react";


interface ScoreItem {
    score : number;
}
  
// 특정 긴글의 점수목록 조회
export function useScores(longTextId:number, isUserFile:boolean){
    const [score, setScore] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<AxiosError | null>(null);

    useEffect(()=>{
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;

        const fetchScores = async () => {
            setLoading(true);
            setError(null);

            try {
                if(isUserFile){
                    const res = await axios.get(`${baseUrl}/my-long-text/${longTextId}/score`, {
                        withCredentials: true,
                    })
                    const scores: ScoreItem[] = res.data.data;
                    const maxScore = scores.reduce((max, cur) => Math.max(max,cur.score), 0);
                    setScore(maxScore)
                }else{
                    const res = await axios.get(`${baseUrl}/long-text/${longTextId}/score`, {
                        withCredentials: true,
                    })
                    const scores: ScoreItem[] = res.data.data;
                    const maxScore = scores.reduce((max, cur) => Math.max(max,cur.score), 0);
                    setScore(maxScore);
                }
            } catch (err){
                setError(err as AxiosError);
            } finally {
                setLoading(false);
            }
        };
        fetchScores();
    }, [longTextId, isUserFile]);
    return {score, loading, error, setScore}
}