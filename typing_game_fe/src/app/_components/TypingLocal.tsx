"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import * as Hangul from "hangul-js";
import ResultModal from "./ResultModal";
import { useTexts } from "../hooks/useTexts";

interface TypingLocalProps {
  lyrics: string;
}

const TypingLocal: React.FC<TypingLocalProps> = ({ lyrics }) => {

  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [elapsedTime, setElapsedTime] = useState(0);
  const [completed, setCompleted] = useState(false);
  const [cpm, setCpm] = useState(0);
  const [correctChars, setCorrectChars] = useState(0);
  const [totalChars, setTotalChars] = useState(0);

  const currentLine = lyrics;

  
  const totalTypedChars = () => {
    // 이전 줄까지 자모 분리 후 평탄화해서 길이 구하기
    const pastChars = Hangul
      .disassemble(lyrics.slice(0, currentLineIndex), true)
      .flat().length;

    // 현재 입력값 자모 분리 후 길이
    const currentInputChars = Hangul
      .disassemble(inputValue, true)
      .flat().length;
    return pastChars + currentInputChars;
  };

  // 입력 이벤트
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {

    if (startTime === null) {
      setStartTime(Date.now());
    }

    if (e.key === "Enter") {
      if (inputValue.length === currentLine.length) {
        if (startTime !== null) {
          const timeTaken = Date.now() - startTime;
          setElapsedTime(prev => prev + timeTaken);
        }
        let correct = 0;
        for (let i = 0; i < currentLine.length; i++) {
          if (inputValue[i] === currentLine[i]) {
            correct++;
          }
        }
        setCorrectChars(prev => prev + correct);
        setTotalChars(prev => prev + currentLine.length);
        setCompleted(true);
      }
    }
  };

  // 다시하기 버튼
  const handleRetry = () => {
    setCompleted(false);
    setCurrentLineIndex(0);
    setInputValue("");
    setStartTime(null);
    setElapsedTime(0);
    setCpm(0);
    setCorrectChars(0);
    setTotalChars(0);
  }

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

  useEffect(() => {
    setCurrentLineIndex(0);
    setInputValue("");
    setStartTime(null);
    setElapsedTime(0);
    setCompleted(false);
    setCpm(0);
    setCorrectChars(0);
    setTotalChars(0);
  }, [lyrics]);

  const accuracy = totalChars > 0 ? Math.round((correctChars / totalChars) * 100) : 0;
  const totalLyricsChars = Hangul
    .disassemble(lyrics, true)
    .flat().length;
  return (
    <>
      <Wrapper>
        {completed && (
          <ResultModal
            accuracy={accuracy}
            cpm={cpm}
            elapsedTime={elapsedTime}
            totalChars={totalChars}
            correctChars={correctChars}
            lineCount={lyrics.length}
            onRetry={handleRetry}
          />
        )}
         <ProgressBarContainer>
          <ProgressBarFill progress={totalTypedChars() / totalLyricsChars * 100} />
        </ProgressBarContainer> 
      <TypingLine>
        <CurrentLine>
          {(currentLine ?? "").split("").map((char, i) => {
            const typedChar = inputValue[i];
            let color = "var(--color-basic)";
          
            let textDecoration = "transparent";
            if (typedChar !== undefined) {
              if (i === inputValue.length - 1) {
                color = 'black';
              } else {
                color = typedChar === char ? "var(--color-correct)" : "var(--color-wrong)";
                if (typedChar !== char) {
                  textDecoration = 'underline'; // 틀린 경우 밑줄
                }
              }
            }
            
            return (
              <CharSpan key={i} style={{ color, textDecoration }}>
                {char}
              </CharSpan>
            );
          })}
        </CurrentLine>
      </TypingLine>
      <Input
        type="text"
        value={inputValue}
        spellCheck={false} // 맞춤법
        disabled={completed}
        onChange={(e) => setInputValue(e.target.value)}
        onPaste={(e) => {e.preventDefault()}} //붙여넣기 막기
        onDrop={(e) => e.preventDefault()} // 드래그앤드롭막기
        onKeyDown={handleKeyDown}
        placeholder="여기에 입력하세요"
      />
      </Wrapper>
    </>
  );
};

export default TypingLocal;

const TypingLine = styled.div`
  min-height: 300px;
`;

const ProgressBarContainer = styled.div`
  // width: 100%;
  height: 2px;
  background-color: var(--progress-bg);
  margin-bottom: 1.5rem;
`;

const ProgressBarFill = styled.div<{ progress: number }>`
  height: 100%;
  background-color: var(--key-fill-red);
  width: ${({ progress }) => progress}%;
  transition: width 0.3s ease;
`;


const Wrapper = styled.div`
  height : 370px;
  min-width: var(--tpg-basic-width);
`;

const CurrentLine = styled.p`
  margin-bottom: 1rem;
  width: 100%;
  font-size: var(--typing-size);
`;

const CharSpan = styled.span`
  transition: color 0.1s;
  font-size: var(--typing-size);
  text-underline-position : under;
`;

const Input = styled.input`
  font-size: var(--typing-size);
  width: 100%;
  outline: none;
  user-select: none;  // 드래그 선택 막기
`;
