// components/MainContent.tsx
import React from 'react';
import styled from 'styled-components';
import TypingGame from './TypingGame';

interface MainContentProps {
  selectedSong: {
    title: string;
    lyrics: string;
  };
}

const MainContent: React.FC<MainContentProps> = ({ header, selectedSong }) => {
  return (
    <MainWrapper>
        <Header>
            {header}
            <RightInfo>로그인</RightInfo>
        </Header>
        <Title>{selectedSong.title}</Title>
        <TypingGame lyrics={selectedSong.lyrics} />
    </MainWrapper>
  );
};

export default MainContent;

const MainWrapper = styled.div`
  padding-left:6rem; padding-right : 6rem; 
  width: 950px;
  color: black;
  height : 43rem;
  background-color: rgba(255, 255, 255, 0.96);
  border-radius : 0 20px 20px 0;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 70px;
  font-size: 0.9rem;
`;


const RightInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
`;


const Title = styled.h1`
  font-size: 2.5rem; /* text-2xl */
  font-weight: bold;
  margin-bottom: 1.5rem; /* mb-6 */
  margin-top : 70px;
`;