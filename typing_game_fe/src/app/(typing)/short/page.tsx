"use client";
import React, { useState } from "react";
import { buckwheatFlower } from "../../../data/buckwheatFlower"; // 여러 가사 목록 데이터
import Sidebar from "../../_components/SideBar";
import TypingGame from "../../_components/TypingGame";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";

import styled from "styled-components";

const TypingPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(buckwheatFlower[0]);
  const [uploadedFiles, setUploadedFiles] = useState<
    { title: string; lyrics: string[] }[]
  >([]);

  const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
    setUploadedFiles((prev) => [...prev, file]);
  };

  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const toggleSidebar = () => setIsSidebarOpen((prev) => !prev);

  return (
    <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={buckwheatFlower}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
        </SidebarWrapper>
      )}
      <Content>
        <Header>
          <span>짧은글연습</span>
          <Title>{selectedSong.title}</Title>
          <RightInfo>로그인</RightInfo>
        </Header>

        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />

        <MainWrapper>
          <TypingGame lyrics={selectedSong.lyrics} />
        </MainWrapper>
      </Content>
    </Box>
  );
};

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

const Content = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  color: black;
  font-size: 0.8rem;
  margin-top: 3rem;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
`;

const RightInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  z-index: 1;
`;

const MainWrapper = styled.div`
  width: 600px;
  color: black;
  border-radius: 0 20px 20px 0;
`;
