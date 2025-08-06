"use client";
import React, { useState } from "react";
import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터
import Sidebar from "../../_components/SideBar"
import MainContent from "../../_components/MainContent"
import Keyboard from "../../_components/Keyboard"

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
    <>
      <Box>
        {/* <Sidebar 
            lyricsList={lyricsList}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          /> */}
        <MainContent header="긴글연습" selectedSong={selectedSong} />
      </Box>
    </>
  );
}
export default TypingPage;


const Box = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center; 
  align-items: center;     
`;


