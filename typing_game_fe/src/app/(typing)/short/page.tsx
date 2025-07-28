"use client";
import React, { useState } from "react";
import { buckwheatFlower } from "../../../data/buckwheatFlower"; // 여러 가사 목록 데이터
import TypingGame from "../../_components/TypingGame";
import Sidebar from "../../_components/SideBar"
import MainContent from "../../_components/MainContent"

import styled from "styled-components";

const TypingPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(buckwheatFlower[0]);
  const [uploadedFiles, setUploadedFiles] = useState<
    { title: string; lyrics: string[] }[]
  >([]);

  const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
    setUploadedFiles((prev) => [...prev, file]);
  };

  return (
    <>
      <Box>
        <Sidebar 
            lyricsList={buckwheatFlower}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
        <MainContent header="짧은 글 연습" selectedSong={selectedSong} />
      </Box>    
    </>
  );
}
export default TypingPage;



const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
  z-index: 1;
`;


