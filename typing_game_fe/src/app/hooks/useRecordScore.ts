
// 점수 기록하기
export function useRecordScore(longTextId: number, isUserFile: boolean, authHeader: string) {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;

  const recordScore = async (score: number) => {
    const url = isUserFile
      ? `${baseUrl}/my-long-text/${longTextId}/score`
      : `${baseUrl}/long-text/${longTextId}/score`;

    const res = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: authHeader,
      },
      body: JSON.stringify({ score }),
    });

    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }

    return res.json();
  };

  return { recordScore };
}
