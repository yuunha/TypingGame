"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import * as Hangul from "hangul-js";
import QuoteResult from "./QuoteResult";

interface TypingLocalProps {
  lyrics: string;
}

const TypingLocal: React.FC<TypingLocalProps> = ({ lyrics }) => {

  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [completed, setCompleted] = useState(false);
  const [cpm, setCpm] = useState(0);
  const [correctChars, setCorrectChars] = useState(0);
  const [totalChars, setTotalChars] = useState(0);
  
  const totalTypedChars = () => 
    Hangul.disassemble(inputValue, true).flat().length;

  // 입력 이벤트
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (startTime === null) setStartTime(Date.now());

    if (e.key === "Enter" && inputValue.length === lyrics.length) {
        if (startTime !== null) {
          const timeTaken = Date.now() - startTime;
          const chars = Hangul.disassemble(inputValue, true).flat();
          let correct = 0;
          for (let i = 0; i < lyrics.length; i++) {
            if (inputValue[i] === lyrics[i]) correct++;
          }
          setCorrectChars(prev => prev + correct);
          setTotalChars(prev => prev + lyrics.length);
          setCpm(Math.round(chars.length / (timeTaken / 60000)));
        }
        setCompleted(true);
    }
  };

  // 다시하기 버튼
  const handleRetry = () => {
    setInputValue("");
    setStartTime(null);
    setCompleted(false);
    setCpm(0);
    setCorrectChars(0);
    setTotalChars(0);
  };

  // 실시간 CPM 계산
  useEffect(() => {
    if (startTime === null || completed) return;

    const interval = setInterval(() => {
      const elapsed = Date.now() - startTime;
      const chars = totalTypedChars();
      setCpm(Math.round(chars / (elapsed / 60000)));
    }, 100);

    return () => clearInterval(interval);
  }, [startTime, inputValue, completed]);

  useEffect(() => {
    handleRetry();
  }, [lyrics]);

  const accuracy = totalChars > 0 ? Math.round((correctChars / totalChars) * 100) : 0;
  const totalLyricsChars = Hangul.disassemble(lyrics, true).flat().length;

  return (
    <>
      <Wrapper>
        {completed && (
          <QuoteResult
            accuracy={accuracy}
            cpm={cpm}
            onRetry={handleRetry}
          />
        )}
         <ProgressBarContainer>
          <ProgressBarFill progress={totalTypedChars() / totalLyricsChars * 100} />
        </ProgressBarContainer> 
      <TypingLine>
        <CurrentLine>
          {lyrics.split("").map((char, i) => {
            const typedChar = inputValue[i];
            let color = "var(--color-basic)";
            let textDecoration = "transparent";

            if (typedChar !== undefined) {
              if (i === inputValue.length - 1) color = "black";
              else {
                color = typedChar === char ? "var(--color-correct)" : "var(--color-wrong)";
                if (typedChar !== char) textDecoration = "underline";
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
  width: var(--tpg-basic-width);

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
