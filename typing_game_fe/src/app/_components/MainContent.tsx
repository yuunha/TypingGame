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

const MainContent: React.FC<MainContentProps> = ({ selectedSong }) => {
  return (
    <MainWrapper>
      <Title>{selectedSong.title}</Title>
      <TypingGame lyrics={selectedSong.lyrics} />
    </MainWrapper>
  );
};

export default MainContent;

const MainWrapper = styled.div`
  padding: 3rem;
  padding-left:6rem; padding-right : 6rem; 
  width: 950px;
  color: black;
  height : 43rem;
  background-color: rgba(255, 255, 255, 0.99);
  border-radius : 0 20px 20px 0;
`;

const Title = styled.h1`
  font-size: 1.5rem; /* text-2xl */
  font-weight: bold;
  margin-bottom: 1.5rem; /* mb-6 */
`;
