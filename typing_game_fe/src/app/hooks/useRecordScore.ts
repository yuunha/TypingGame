import axios from "axios";

// 점수 기록하기

export function useRecordScore(longTextId: number, isUserFile: boolean, authHeader: string) {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;

  const recordScore = async (score: number) => {
    const url = isUserFile
      ? `${baseUrl}/my-long-text/${longTextId}/score`
      : `${baseUrl}/long-text/${longTextId}/score`;

    try {
      const res = await axios.post(
        url,
        { score }, // 여기서 실제 보낼 데이터
        {
          headers: { Authorization: authHeader },
          withCredentials: true,
        }
      );
      console.log("성공", res.data);
      return res.data;
    } catch (err) {
      console.log("실패", err);
      throw err; // 필요하면 호출한 곳에서 catch 가능
    }
  };

  return { recordScore };
}
