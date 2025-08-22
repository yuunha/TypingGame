"use client";
import React, { useState, useEffect } from "react";
import Sidebar from "../../_components/SideBar";
import Typing from "../../_components/Typing";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";
import styled from "styled-components";
import { useAuth } from "@/app/hooks/useAuth";
import { useLongTexts, LongText } from "@/app/hooks/useLongTexts";

const TypingPage: React.FC = () => {
  const { isLoggedIn } = useAuth();
  const lyricsList = useLongTexts();
  const [selectedSong, setSelectedSong] = useState<LongText | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

    // selectedSong이 이미 있으면 바뀌지 않도록
  useEffect(() => {
    if (lyricsList.length > 0 && !selectedSong) setSelectedSong(lyricsList[0]);
  }, [lyricsList]);


  return (
    <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={lyricsList}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            isLoggedIn={isLoggedIn}
          />
        </SidebarWrapper>
      )}
      <Content>
        {selectedSong && (
          <Header>
            <span> </span>
            <Title>{selectedSong.title}</Title>
            <RightInfo> </RightInfo>
          </Header>
        )}
        {selectedSong && (
          <MainWrapper>
            <Typing
              longTextId={selectedSong.longTextId ?? 0}
              isUserFile={selectedSong.isUserFile ?? false}
            />
          </MainWrapper>
        )}
        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />
      </Content>
    </Box>
  );
};

export default TypingPage;

const Box = styled.div`
  position: relative;
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
