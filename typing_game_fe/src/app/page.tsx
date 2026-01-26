import keys from '@/features/keyboard/keys'
import QuoteTyping from "@/components/QuoteTyping";
import Keyboard from '@/features/keyboard/Keyboard';

const FALLBACK_QUOTE = {
  author: "서버 연결 문제",
  content: "오늘의 글을 불러올 수 없습니다."
};

export default async function Home() {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;

  let quote = FALLBACK_QUOTE;

  try {
    const res = await fetch(`${baseUrl}/quote/today`, {
      cache: "no-store", //revalidate는 빌드시 서버와 연결 필요
    });

    if (!res.ok) throw new Error("Fetch failed");

    quote = await res.json();
    // TODO: 콘솔 확인 후 안정성 패치
  } catch (e) {

  }

return (
    <div className="content">
      <div className="header">
        <h1 className="title">오늘의 글</h1>
        <h1 className="title">{quote.author ?? ""}</h1>
      </div>
      <div className="mainWrapper">
        <QuoteTyping lyrics={quote.content ?? ""} />
      </div>
      <Keyboard keys={keys} />
    </div>
  );
}
