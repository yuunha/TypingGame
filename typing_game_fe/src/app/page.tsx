'use client';

import Image from "next/image";
import styled from "styled-components";
import TypingGame from "../app/_components/TypingGame";
import TypingPage from "./(typing)/long/TypingPage";
import NavigationBar from "../app/_components/NavigationBar";
import '@/styles/font/font.css';

export default function Home() {
  return (
    <Container>
      <Background />

      <SideBox>
        <IconGrid>
          <IconBox>
            <IconText>긴</IconText>
          </IconBox>
          <IconBox width="12rem">
            <IconText>게임</IconText>
          </IconBox>
          <IconBox>
            <IconText>짧</IconText>
          </IconBox>
          <BlankBox></BlankBox>
          <IconBox color="#274A74">
            <IconText>낱</IconText>
          </IconBox>
          <BlankBox></BlankBox>
          <IconBox color="#274A74">
            <IconText>랭</IconText>
          </IconBox>
        </IconGrid>
      </SideBox>
      <CenterBox></CenterBox>
    </Container>
  );
}


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

const SideBox = styled.div`
  position: absolute;
  top: 50%;
  left: 300px;
  transform: translateY(-50%);
  display: flex;
  width: 500px;
`;
const CenterBox = styled.div`
  position: absolute;
  top: 50%;
  left: 300px;
  transform: translateY(-50%);
  display: flex;
  width: 500px;
`;
const IconGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(2, 1fr); // 3열짜리 그리드
  gap: 20px;
  width: 300px;
`;

const BlankBox = styled.div`
  width: 8rem;
  height: 8rem;
`;

const IconBox = styled.div<{ width?: string; color?: string }>`
  width: ${({ width }) => width || '8rem'};
  height: 8rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.2s ease;
  cursor: pointer;

  border-radius: 2rem;
  color: ${({ color }) => color || 'white'};
  font-size: 1.2rem;

  border: 1px solid rgba(255, 255, 255, 0.5);
  // background-color: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(10px) saturate(120%);
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);

  // background: rgba(255, 255, 255, 0.15);
  // backdrop-filter: blur(2px) saturate(180%);
  // border: 1px solid rgba(255, 255, 255, 0.8);
  // box-shadow:
  //   0 8px 32px rgba(31, 38, 135, 0.2),
  //   inset 0 4px 20px rgba(255, 255, 255, 0.3);

  &::after {
    content: '';
    position: absolute;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 2rem;
    backdrop-filter: blur(1px);
    box-shadow:
      inset -10px -8px 0px -11px rgba(255, 255, 255, 1),
      inset 0px -9px 0px -8px rgba(255, 255, 255, 1);
    opacity: 1;
    z-index: -1;
    filter: blur(1px) drop-shadow(10px 4px 6px white) brightness(115%);
    pointer-events: none;
  }
    
  &:hover {
    transform: scale(0.95);
    // background: rgba(255, 255, 255, 0.1);
  }
`;



const IconText = styled.span`
  font-size: 5rem;
  font-weight: 600;
`;
