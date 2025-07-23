"use client";
import React, { useState } from "react";
import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터
import TypingGame from "../../_components/TypingGame";
import Sidebar from "../../_components/SideBar"


const TypingPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(lyricsList[0]);
  const [uploadedFiles, setUploadedFiles] = useState<
    { title: string; lyrics: string[] }[]
  >([]);

  const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
    setUploadedFiles((prev) => [...prev, file]);
  };

  return (
    <div className="flex h-screen">
      {/* 왼쪽 탭 */}
      <aside className="w-64 min-w-[16rem]">
        <Sidebar 
            lyricsList={lyricsList}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
      </aside>
        

      {/* 오른쪽 메인 */}
      <main className="flex-1 p-6 overflow-auto">
        <h1 className="text-2xl font-bold mb-6">{selectedSong.title}</h1>
         <TypingGame lyrics={selectedSong.lyrics} />
      </main>
    </div>
  );
}
export default TypingPage;
