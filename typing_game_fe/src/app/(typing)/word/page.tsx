"use client";
import React, { useState } from "react";
import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터
import TypingGame from "../../_components/TypingGame";
import Sidebar from "../../_components/SideBar"
import MainContent from "../../_components/MainContent"

import styled from "styled-components";

const TypingPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(lyricsList[0]);
  const [uploadedFiles, setUploadedFiles] = useState<
    { title: string; lyrics: string[] }[]
  >([]);

  const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
    setUploadedFiles((prev) => [...prev, file]);
  };

  return (
    <Container>
      <Background />
      <Box>
        <Sidebar 
            lyricsList={lyricsList}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
        <MainContent header="낱말 연습" selectedSong={selectedSong} />

      </Box>
    </Container>
  );
}
export default TypingPage;

const Container = styled.div`
  position: relative;
  width: 100%;
  height: 100vh;
`;

const Background = styled.div`
  position: absolute;
  inset: 0;
  background-image: url('/background.jpg');
  background-size: cover;
  background-position: center;
  background-color: white;
`;

const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
  z-index: 1;
`;


