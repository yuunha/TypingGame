"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import TypingCat from "./TypingCat";

interface TypingGameProps {
  lyrics: string[];
}

const TypingGame: React.FC<TypingGameProps> = ({ lyrics }) => {
  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [completed, setCompleted] = useState(false);

  const [isTyping, setIsTyping] = useState(false);

  useEffect(() => {
    let typingTimeout: ReturnType<typeof setTimeout>;
    const handle = () => {
      setIsTyping(true);
      clearTimeout(typingTimeout);
      typingTimeout = setTimeout(() => setIsTyping(false), 100);
    };
    window.addEventListener("keydown", handle);
    return () => {
      window.removeEventListener("keydown", handle);
      clearTimeout(typingTimeout);
    };
  }, []);

  const m2Line = currentLineIndex > 1 ? lyrics[currentLineIndex - 2] : null;
  const m1Line = currentLineIndex > 0 ? lyrics[currentLineIndex - 1] : null;
  const currentLine = lyrics[currentLineIndex];
  const p1Line = currentLineIndex < lyrics.length ? lyrics[currentLineIndex + 1] : null;

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!startTime) setStartTime(Date.now());
    const value = e.target.value;
    setInputValue(value);

    if (value === currentLine) {
      if (currentLineIndex < lyrics.length - 1) {
        setCurrentLineIndex(currentLineIndex + 1);
        setInputValue("");
      } else {
        setCompleted(true);
      }
    }
  };

  const totalTypedChars =
    lyrics.slice(0, currentLineIndex).join("").length + inputValue.length;

  const cpm = startTime
    ? Math.round(totalTypedChars / ((Date.now() - startTime) / 60000))
    : 0;

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
                color = typedChar === char ? "#3B82F6" : "#EF4444"; // blue-500 / red-500
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
        <p>최고 속도: {cpm} 타</p>
        <p>평균 속도: {cpm} 타</p>
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
