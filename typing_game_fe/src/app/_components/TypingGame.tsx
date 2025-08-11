"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import * as Hangul from "hangul-js";
import ResultModal from "./ResultModal";

interface TypingGameProps {
  lyrics: string[];
}

const TypingGame: React.FC<TypingGameProps> = ({ longTextId, lyrics }) => {
  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [elapsedTime, setElapsedTime] = useState(0); // 누적 시간
  const [completed, setCompleted] = useState(false);
  const [cpm, setCpm] = useState(0);
  const [correctChars, setCorrectChars] = useState(0);
  const [totalChars, setTotalChars] = useState(0);

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
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {

    if (startTime === null) {
      setStartTime(Date.now());
    }

    if (e.key === "Enter") {
      if (inputValue.length === currentLine.length) {
        if (startTime !== null) {
          const timeTaken = Date.now() - startTime;
          setElapsedTime(prev => prev + timeTaken);
          setStartTime(null); // 다음 줄부터 다시 시작
        }

        let correct = 0;
        for (let i = 0; i < currentLine.length; i++) {
          if (inputValue[i] === currentLine[i]) {
            correct++;
          }
        }

        setCorrectChars(prev => prev + correct);
        setTotalChars(prev => prev + currentLine.length);


        if (currentLineIndex < lyrics.length - 1) {
          setCurrentLineIndex(prev => prev + 1);
          setInputValue("");
        } else {
          setCompleted(true);
        }
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
    setCompleted(false);
    setCpm(0);
    setCorrectChars(0);
    setTotalChars(0);
  }

  //복붙드래그앤드롭막기
  useEffect(() => {
  const handlePaste = (e: ClipboardEvent) => {
    e.preventDefault();
  };
  const handleDrop = (e: DragEvent) => {
    e.preventDefault();
  };

  document.addEventListener("paste", handlePaste);
  document.addEventListener("drop", handleDrop);

  return () => {
    document.removeEventListener("paste", handlePaste);
    document.removeEventListener("drop", handleDrop);
  };
}, []);

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
    .disassemble(lyrics.join(""), true)
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
            longTextId = {longTextId}
          />
        )}
        <ProgressBarContainer>
          <ProgressBarFill progress={totalTypedChars() / totalLyricsChars * 100} />
        </ProgressBarContainer>
      <TypingLine>
        {m1Line && <SubLine>{m1Line}</SubLine>}

        <CurrentLine>
          {currentLine.split("").map((char, i) => {
            const typedChar = inputValue[i];
            let color = "";

            if (typedChar !== undefined) {
              if (i === inputValue.length - 1) {
                color = 'black';
              } else {
                color = typedChar === char ? "var(--color-correct)" : "var(--color-wrong)";
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
          spellCheck={false} // 맞춤법
          onChange={(e) => setInputValue(e.target.value)}
          onPaste={(e) => {e.preventDefault()}} //붙여넣기 막기
          onDrop={(e) => e.preventDefault()} // 드래그앤드롭막기
          onKeyDown={handleKeyDown}
          placeholder="여기에 입력하세요"
        />

        <InfoBox>
          <p>평균 {cpm} 타</p>
          <p>정확도 {cpm} 타</p>
        </InfoBox>
      </Wrapper>
    
    </>
  );
};

export default TypingGame;

const TypingLine = styled.div`
  min-height: 140px;
`;

const ProgressBarContainer = styled.div`
  width: 100%;
  height: 8px;
  background-color: var(--progress-bg);
  border-radius: 6px;
  overflow: hidden;
  margin-bottom: 1.5rem;
`;

const ProgressBarFill = styled.div<{ progress: number }>`
  height: 100%;
  background-color: var(--progress-fill);
  width: ${({ progress }) => progress}%;
  transition: width 0.3s ease;
`;


const Wrapper = styled.div`
  flex-direction: column;
  align-items: center;
  margin-top: 2rem;
  height : 400px;
  min-width: 600px;
  p {
    font-size: 15px;
  }
`;


const SubLine = styled.div`
  margin-bottom: 1rem;
  width: 100%;
  font-size: var(--typing-size);
  color: var(--typing-line-sub);
`;

const CurrentLine = styled.p`
  margin-bottom: 1rem;
  width: 100%;
  font-size: var(--typing-size);
`;

const CharSpan = styled.span`
  transition: color 0.1s;
  font-size: var(--typing-size);
`;

const Input = styled.input`
  font-size: var(--typing-size);
  width: 100%;
  outline: none;
  user-select: none;  // 드래그 선택 막기
`;

const InfoBox = styled.div`
  display: flex;
  gap: 1rem;
  margin-top: 3rem;
  font-size: 1.125rem;
`;


// 결과창
const ResultStats = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 16px 0 16px 0;
`;

const StatBox = styled.div`
  background-color: #2c2c2c;
  padding: 8px 10px;
  border-radius: 6px;
  font-size: 14px;
  white-space: nowrap;
  color: #ffffffff;
  font-size: 0.8rem;
`;

const RetryButton = styled.button`

  padding: 8px 10px;
  border-radius: 6px;
  font-size: 14px;
  white-space: nowrap;
  font-size: 0.8rem;

  cursor: pointer;
  transition: background-color 0.2s ease;

  &:hover {
    // background-color: #d4d4d8;
  }
`;