'use client';

import styled from "styled-components";
import Link from 'next/link';

import '../app/globals.css';

export default function Home() {
  return (
    <>
      <SideBox>
        <IconGrid>
          <Link href="/long" passHref>
            <IconBox>
              <IconText>긴</IconText>
            </IconBox>
          </Link>

          <Link href="/short" passHref>
            <IconBox width="12rem">
              <IconText>게임</IconText>
            </IconBox>
          </Link>

          <Link href="/short" passHref>
          <IconBox>
            <IconText>짧</IconText>
          </IconBox>
          </Link>

          <BlankBox></BlankBox>
          
          <Link href="/word" passHref>
            <IconBox color="var(--color-point)">
              <IconText>낱</IconText>
            </IconBox>
          </Link>

          <BlankBox></BlankBox>
          
          <Link href="/rank" passHref>
            <IconBox color="var(--color-point)">
              <IconText>랭</IconText>
            </IconBox>
          </Link>

        </IconGrid>
      </SideBox>
      <CenterBox></CenterBox>
      </>
  );
}



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

const IconBox = styled.a<{ width?: string; color?: string }>`
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
