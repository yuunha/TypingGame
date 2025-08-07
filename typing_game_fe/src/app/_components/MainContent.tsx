// components/MainContent.tsx
import React from 'react';
import styled from 'styled-components';
import TypingGame from './TypingGame';
import Keyboard from './Keyboard';
import typingKeys from './keyboard/typingKeys';

interface MainContentProps {
  header: string;
  selectedSong: {
    title: string;
    lyrics: string | string[];
  };
  onToggleSidebar: () => void;
}

const MainContent: React.FC<MainContentProps> = ({ header, selectedSong, onToggleSidebar}) => {
  console.log(header)
  return (
    <>
    <Header>
      {header}
      <Title>{selectedSong.title}</Title>
      <RightInfo>로그인</RightInfo>
    </Header>
    <Keyboard keys = {typingKeys} onToggleSidebar={onToggleSidebar}/>
    <MainWrapper>
      <TypingGame lyrics={selectedSong.lyrics} />
    </MainWrapper>
    </>
  );
};

export default MainContent;

const MainWrapper = styled.div`
  width: 600px;
  color: black;
  border-radius: 0 20px 20px 0;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  // height: 100px;
  color: black;
  font-size: 0.8rem;
  margin-top:3rem;
`;


const RightInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  z-index:1;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
  font-weight:bold;
`;