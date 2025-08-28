import axios from "axios";

// 점수 기록하기

export function useRecordScore(longTextId: number, isUserFile: boolean, authHeader: string) {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;

  const recordScore = async (score: number) => {
    const url = isUserFile
      ? `${baseUrl}/my-long-text/${longTextId}/score`
      : `${baseUrl}/long-text/${longTextId}/score`;

    fetch(url,{
      headers: { Authorization: authHeader },
      credentials: "include",
      body : JSON.stringify({ score })
    })
    .then((res)=> res.json())
    .catch((err)=> console.error(err))
  };

  return { recordScore };
}
