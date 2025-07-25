"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import TypingCat from "./TypingCat";
import * as Hangul from "hangul-js";

interface TypingGameProps {
  lyrics: string[];
}

const TypingGame: React.FC<TypingGameProps> = ({ lyrics }) => {
  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [elapsedTime, setElapsedTime] = useState(0); // 누적 시간
  const [completed, setCompleted] = useState(false);
  const [cpm, setCpm] = useState(0);

  const m1Line = currentLineIndex > 0 ? lyrics[currentLineIndex - 1] : null;
  const currentLine = lyrics[currentLineIndex];
  const p1Line = currentLineIndex < lyrics.length ? lyrics[currentLineIndex + 1] : null;

  const totalTypedChars = () => {
    // 이전 줄까지 자모 분리 후 평탄화해서 길이 구하기
    const pastChars = Hangul
      .disassemble(lyrics.slice(0, currentLineIndex).join(""), true)
      .flat().length;

    // 현재 입력값 자모 분리 후 길이
    const currentInputChars = Hangul
      .disassemble(inputValue, true)
      .flat().length;

    return pastChars + currentInputChars;
  };

  // 입력 이벤트
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;

    if (startTime === null) {
      setStartTime(Date.now());
    }

    setInputValue(value);

    if (value === currentLine) {
      if (startTime !== null) {
        const timeTaken = Date.now() - startTime;
        setElapsedTime(prev => prev + timeTaken);
        setStartTime(null); // 다음 줄부터 다시 시작
      }

      if (currentLineIndex < lyrics.length - 1) {
        setCurrentLineIndex(prev => prev + 1);
        setInputValue("");
      } else {
        setCompleted(true);
      }
    }
  };

  // 실시간 CPM 계산
  useEffect(() => {
    if (startTime === null || completed) return;

    const interval = setInterval(() => {
      const now = Date.now();
      const elapsed = elapsedTime + (now - startTime);
      const chars = totalTypedChars();
      const currentCpm = elapsed > 0 ? Math.round(chars / (elapsed / 60000)) : 0;
      setCpm(currentCpm);
    }, 100);

    return () => clearInterval(interval);
  }, [startTime, inputValue, currentLineIndex, elapsedTime, completed]);


  if (completed) {
    return (
      <ResultContainer>
        <h2>🎉 완료!</h2>
        <p>속도: {cpm}타</p>
      </ResultContainer>
    );
  }

  return (
    <Wrapper>
      {/* <TypingCat isTyping={isTyping} /> */}

      {/* {m2Line && <SubLine>{m2Line}</SubLine>} */}

      <TypingLine>
        {m1Line && <SubLine>{m1Line}</SubLine>}

        <CurrentLine>
          {currentLine.split("").map((char, i) => {
            const typedChar = inputValue[i];
            let color = "";

            if (typedChar !== undefined) {
              if (i === inputValue.length - 1) {
                color = "#000000"; // 기본색
              } else {
                color = typedChar === char ? "#3B82F6" : "#EF4444";
              }
            }

            return (
              <CharSpan key={i} style={{ color: color || "inherit" }}>
                {char}
              </CharSpan>
            );
          })}
        </CurrentLine>

        {p1Line && <SubLine>{p1Line}</SubLine>}
      </TypingLine>
      <Input
        type="text"
        value={inputValue}
        onChange={handleChange}
        placeholder="여기에 입력하세요"
      />

      <InfoBox>
        {/* <p>최고 속도: {cpm} 타</p> */}
        <p>평균 속도: {cpm} 타</p>
        <p>정확도: {cpm} 타</p>
      </InfoBox>
      <ProgressBarContainer>
        <ProgressBarFill progress={(currentLineIndex ) / lyrics.length * 100} />
      </ProgressBarContainer>
    </Wrapper>
  );
};

export default TypingGame;

const TypingLine = styled.div`
  height: 150px;
`;

const ProgressBarContainer = styled.div`
  width: 100%;
  height: 12px;
  background-color: #e5e7eb; // gray-200
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 1.5rem;
`;

const ProgressBarFill = styled.div<{ progress: number }>`
  height: 100%;
  background-color: var(--color-primary, #3B82F6); // 파란색 또는 CSS 변수
  width: ${({ progress }) => progress}%;
  transition: width 0.3s ease;
`;


const Wrapper = styled.div`
  // display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 5rem;
  height : 500px;
  min-width: 600px;
`;

const ResultContainer = styled.div`
  margin-top: 5rem;
  text-align: center;

  h2 {
    font-size: 2rem;
    font-weight: bold;
  }

  p {
    margin-top: 1rem;
    font-size: 1.25rem;
  }
`;

const SubLine = styled.p`
  font-size: 1.25rem;
  margin-bottom: 1rem;
  color: #9ca3af; // Tailwind gray-400
  width: 100%;
`;

const CurrentLine = styled.p`
  font-size: 1.25rem;
  margin-bottom: 1rem;
  width: 100%;
`;

const CharSpan = styled.span`
  transition: color 0.1s;
`;

const Input = styled.input`
  border-radius: 0.5rem;
  font-size: 1.25rem;
  width: 100%;
  outline: none;
`;

const InfoBox = styled.div`
  display: flex;
  gap: 1rem;
  margin-top: 3rem;
  font-size: 1.125rem;
`;
