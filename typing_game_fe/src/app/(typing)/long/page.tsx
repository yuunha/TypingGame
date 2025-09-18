"use client";
import React, { useState, useEffect, createContext } from "react";
import Sidebar from "../../_components/SideBar";
import Typing from "./Typing";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";
import styled from "styled-components";
import { useAuth } from "@/app/hooks/useAuth";
import { LongText } from "@/app/types/long-text";
import { useLongTexts } from "@/app/hooks/useLongTexts";

export const ItemContext = createContext<{
  selectedText: LongText | null;
  setSelectedText: React.Dispatch<React.SetStateAction<LongText | null>>;
}>({
  selectedText: null,
  setSelectedText: () => {},
});


const TypingPage: React.FC = () => {
  const { isLoggedIn } = useAuth();
  const lyricsList = useLongTexts();
  const [selectedText, setSelectedText] = useState<LongText | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);

  // selectedSong이 이미 있으면 바뀌지 않도록
  useEffect(() => {
    if (lyricsList.length > 0 && !selectedText) setSelectedText(lyricsList[0]);
  }, [lyricsList]);

  return (
    <ItemContext.Provider value={{ selectedText, setSelectedText }}>
    <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={lyricsList}
            isLoggedIn={isLoggedIn}
          />
        </SidebarWrapper>
      )}
      <Content>
        {selectedText ? (
          <>
            <Header>
              <Title>긴글연습</Title>
              <Title>〈{selectedText.title}〉</Title>
            </Header>
            <MainWrapper>
              <Typing
                longTextId={selectedText.longTextId ?? 0}
                isUserFile={selectedText.isUserFile ?? false}
              />
            </MainWrapper>
          </>
        ) : (
          <>
            <Header>
              <Title>긴글연습</Title>
              <Title>선택된 글이 없습니다</Title>
            </Header>
          <MainWrapper>
            <ProgressBarContainer/>
            <EmptyMessage>아직 선택된 글이 없습니다.
              <SubMessage>왼쪽 사이드바에서 글을 선택해주세요!</SubMessage>
            </EmptyMessage>
            
          </MainWrapper>
          </>
        )}
        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />
      </Content>
    </Box>
    </ItemContext.Provider>
  );
};

export default TypingPage;

const Box = styled.div`
  position: relative;
  align-items: center;
  height: 100vh;
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
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  font-size: var(--tpg-header-font-size);
  padding : 0 2px ;
`;

const Title = styled.h1`
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const MainWrapper = styled.div`
  width: var(--tpg-basic-width);
`;

const EmptyMessage = styled.h2`
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  min-height: 300px;
`;

const SubMessage = styled.p`
  font-size: 0.9rem;
  color: #666;
`;

const ProgressBarContainer = styled.div`
  height: 2px;
  background-color: var(--progress-bg);
  margin-bottom: 1.5rem;
`;
