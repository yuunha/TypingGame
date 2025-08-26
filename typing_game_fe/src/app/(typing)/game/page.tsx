"use client";
import React, { useState, useEffect, useRef } from "react";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";
import styled from "styled-components";


//TODO : words 받아오기, 난이도별 설정
const WORDS = [
  "사과","바나나","컴퓨터","타자","게임","연습","산성비",
  "코딩","강아지","고양이","학교","책상","연필","공부","친구",
  "자동차","자전거","노트북","스마트폰","음악",
];

interface FallingChar {
  char: string;
  x: number;
  y: number;
}
interface WordProps {
  x: number;
  y: number;
  color: string;
}

const TypingGame: React.FC = () => {
  const [fallingChars, setFallingChars] = useState<FallingChar[]>([]);
  const [score, setScore] = useState(0);
  const [isGameStarted, setGameStarted] = useState(false);
  const [isComposing, setIsComposing] = useState(false);

  const inputRef = useRef<HTMLInputElement>(null);
  const generateRef = useRef<ReturnType<typeof setInterval> | null>(null);
  const dropRef = useRef<ReturnType<typeof setInterval> | null>(null);


  const startGame = () => {
    setGameStarted(true);
    setScore(0);
    setFallingChars([]);

    if (generateRef.current) clearInterval(generateRef.current);
    if (dropRef.current) clearInterval(dropRef.current);

    generateRef.current = setInterval(() => {
      const word = WORDS[Math.floor(Math.random() * WORDS.length)];
      const x = Math.random() * 430;
      setFallingChars(prev => [...prev, { char: word, x, y: 0 }]);
    }, 1500);
    //1.5초마다 단어 생성

    dropRef.current = setInterval(() => {
      setFallingChars(prev => 
        prev
          .map(c => ({ ...c, y: c.y + 2 }))
          .filter(c => {
            if (c.y > 450) {
              endGame();
              return false;
            }
            return true;
          })
      );
    }, 30);
    //0.3초마다 y좌표 증가
  };

  // 게임 종료
  const endGame = () => {
    setGameStarted(false);
    if (generateRef.current) clearInterval(generateRef.current);
    if (dropRef.current) clearInterval(dropRef.current);
    generateRef.current = null;
    dropRef.current = null;
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      const value = inputRef.current?.value;
      if (!value) return;

      setFallingChars(prev => {
        const matched = prev.find(c => c.char.startsWith(value));
        if (matched && matched.char === value) {
          setScore(s => s + 1);
          if (inputRef.current) inputRef.current.value = "";
          return prev.filter(c => c !== matched);
        }
        return prev;
      });
    }
  };



  return (
    <Box>
      <GameContainer>
        {!isGameStarted && <StartBtn onClick={startGame}>시작하기</StartBtn>}
        {fallingChars.map((c, i) => (
          <Word
            key={i} x={c.x} y={c.y}
            color={inputRef.current?.value && c.char.startsWith(inputRef.current.value) ? "green" : "red"}
          >{c.char}
          </Word>
        ))}
        <Input
          type="text"
          ref={inputRef}
          disabled={!isGameStarted}
          onKeyDown={handleKeyDown} 
          autoFocus
          spellCheck={false}
          onPaste={(e) => {e.preventDefault()}}
          onDrop={(e) => e.preventDefault()}
          placeholder="단어를 입력하세요"
        />
        <ScoreBoard>점수: {score}</ScoreBoard>
      </GameContainer>
    </Box>
  );
}


export default TypingGame;

const Box = styled.div`
  position: relative;
  align-items: center;
`;

const GameContainer = styled.div`
  width: 30rem;
  height: 30rem;
  border: 2px solid black;
  position: relative;
  overflow: hidden;
  margin: 0 auto;
`

const StartBtn = styled.button`
 position: absolute; 
  top: 50%;
  left : 50%;
  transform: translate(-50%, -50%);
  width : 10rem;
  height : 3rem;
  cursor: pointer;
  font-size:30px;
  font-weight:bold;
  color:red;
`


const Word = styled.div<WordProps>`
  position: absolute;
  left: ${(props) => props.x}px;
  top: ${(props) => props.y}px;
  font-weight: bold;
  font-size: 1rem;
  color: ${(props) => props.color};
`;

const Input = styled.input`
  position: absolute;
  bottom : 10px;
  left : 50%;
  transform: translateX(-50%);
  font-size: var(--typing-size);
  width: 10rem;
  outline: none;
  user-select: none;
`;

const ScoreBoard = styled.div`
  position: absolute; 
    top: 10px;
  left : 10px;
`