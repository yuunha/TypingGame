// components/MainContent.tsx
import React from 'react';
import styled from 'styled-components';
import TypingGame from './TypingGame';
import Keyboard from './Keyboard';

interface MainContentProps {
  header: string;
  selectedSong: {
    title: string;
    lyrics: string;
  };
}

const MainContent: React.FC<MainContentProps> = ({ header, selectedSong }) => {
  console.log(header)
  return (
    <>
    <Header>
      {header}
      <RightInfo>로그인</RightInfo>
    </Header>
    <Title>{selectedSong.title}</Title>
    <Keyboard/>
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
  // background-color: rgba(255, 255, 255, 0.96);
  border-radius: 0 20px 20px 0;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 100px;
  color: black;
  font-size: 0.8rem;
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
  margin-bottom: 1.5rem;
  text-align: center;
  width: 100%;
  font-weight:bold;
`;