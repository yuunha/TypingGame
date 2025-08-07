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

  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

  return (
    <>
      <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={lyricsList}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
        </SidebarWrapper>
      )}
      <div>
        <MainContent
          header="긴글연습"
          selectedSong={selectedSong}
          onToggleSidebar={toggleSidebar}
        />
      </div>
    </Box>
    </>
  );
}
export default TypingPage;


const Box = styled.div`
  position: relative;      
  display: flex;
  justify-content: center; 
  align-items: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
`;

const SidebarWrapper = styled.div`
  position: absolute;
  left: 0;
  top: 0;

`;