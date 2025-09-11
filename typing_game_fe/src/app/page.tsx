import keys from './_components/keyboard/keys'
import QuoteTyping from "./_components/QuoteTyping";
import Keyboard from './_components/Keyboard';

export default async function Home() {
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const res = await fetch(`${baseUrl}/quote/today`, {cache: "no-store"});
  const quote = await res.json();

return (
    <div className="content">
      <div className="header">
        <h1 className="title">오늘의 글</h1>
        <h1 className="title">{quote.author}</h1>
      </div>
      <div className="mainWrapper">
        <QuoteTyping lyrics={quote.content ?? ""} />
      </div>
      <Keyboard keys={keys} />
    </div>
  );
}
