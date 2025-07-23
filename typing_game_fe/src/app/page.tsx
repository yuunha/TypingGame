import Image from "next/image";
import TypingGame from "../app/_components/TypingGame";
import TypingPage from "../app/_components/TypingPage";
import NavigationBar from "../app/_components/NavigationBar";

export default function Home() {
  return (
    <div className="relative w-full h-screen">
      {/* 배경 이미지 */}
      <div
        className="absolute inset-0 bg-cover bg-center"
        style={{
          backgroundImage: "url('/background.jpg')",
          backgroundColor: "white",
        }}
      ></div>

      {/* 중앙 네모 박스 */}
      <div className="relative z-10 flex items-center justify-center h-full">
        {/* <div className="bg-white/10 backdrop-blur-md border border-white/30 p-10 rounded-2xl shadow-2xl text-center h-[700px] w-[1200px] flex flex-col items-center justify-center"> */}
        {/* </div> */}
          {/* 아이콘 그리드 */}
          <div className="grid grid-cols-4 gap-6 p-10">
          {Array.from({ length: 12 }).map((_, i) => (
            <div
              key={i}
              className="w-24 h-24 rounded-2xl bg-white/20 backdrop-blur-sm border border-white/30 shadow-inner shadow-white/30 flex items-center justify-center text-sm font-semibold text-white hover:scale-105 transition-transform duration-200"
            >
              
              <span className="text-white font-semibold">앱 {i + 1}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}


