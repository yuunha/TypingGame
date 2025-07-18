"use client";
import React, { useState } from "react";
import { lyricsList } from "../data/lyricsList"; // 여러 가사 목록 데이터
import TypingGame from "./TypingGame";

const TypingPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(lyricsList[0]);

  return (
    <div className="flex h-screen">
      {/* 왼쪽 탭 */}
      <aside className="w-64 bg-gray-100 border-r p-4 overflow-y-auto">
        <h2 className="text-xl font-bold mb-4">가사 목록</h2>
        <ul>
          {lyricsList.map((song, index) => (
            <li
              key={index}
              onClick={() => setSelectedSong(song)}
              className={`p-2 rounded cursor-pointer mb-2 ${
                selectedSong.title === song.title
                  ? "bg-blue-500 text-white"
                  : "hover:bg-gray-200"
              }`}
            >
              {song.title}
            </li>
          ))}
        </ul>
      </aside>

      {/* 오른쪽 메인 */}
      <main className="flex-1 p-6">
        <h1 className="text-2xl font-bold mb-6">{selectedSong.title}</h1>
         <TypingGame lyrics={selectedSong.lyrics} />
      </main>
    </div>
  );
}
export default TypingPage;
