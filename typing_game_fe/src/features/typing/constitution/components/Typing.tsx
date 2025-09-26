"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import * as Hangul from "hangul-js";
import ResultModal from "@/app/(typing)/constitution/ResultModal";
import { splitByLength } from "@/utils/splitByLength";

interface TypingProps {
  content: string;
  articleIndex: number;
  lastPosition: number;
  saveProgress: (articleIndex: number, lastPosition: number) => void;
}

const Typing: React.FC<TypingProps> = ({ content, articleIndex, lastPosition, saveProgress }) => {
  // useMemo?
  const lines = splitByLength(content, 43);

  // useMemo?
  const initialLineIndex = (() => {
    let accumulated = 0;
    for (let i = 0; i < lines.length; i++) {
      accumulated += lines[i].length;
      if (accumulated > lastPosition) {
        return i;
      }
    }
    return lines.length - 1;
  })();

  // 상태를 하나의 객체로
  // const [state, setState] = useState({
  //   currentLineIndex: initialLineIndex,
  //   inputValue: "",
  //   correctChars: lastPosition,
  //   totalChars: lastPosition,
  //   startTime: null as number | null,
  //   elapsedTime: 0,
  //   completed: false,
  //   cpm: 0
  // });

  const [currentLineIndex, setCurrentLineIndex] = useState<number>(initialLineIndex);
  const [inputValue, setInputValue] = useState<string>(""); // 항상 빈 문자열
  const [correctChars, setCorrectChars] = useState<number>(lastPosition);
  const [totalChars, setTotalChars] = useState<number>(lastPosition);

  const currentLine = lines[currentLineIndex];
  const nextLines = lines.slice(currentLineIndex + 1, currentLineIndex + 3);

  const [startTime, setStartTime] = useState<number | null>(null);
  const [elapsedTime, setElapsedTime] = useState(0);
  const [completed, setCompleted] = useState(false);

  const [cpm, setCpm] = useState(0);


  const totalTypedChars = () => {
    const pastChars = Hangul
      .disassemble(lines.slice(0, currentLineIndex).join(""), true)
      .flat().length;

    const currentInputChars = Hangul
      .disassemble(inputValue, true)
      .flat().length;
    return pastChars + currentInputChars;
  };

  // useCallback?
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
  
    if (startTime === null) {
      setStartTime(Date.now());
    }

    if (e.key === "Enter") {
      if (inputValue.length === currentLine.length) {
        if (startTime !== null) {
          const timeTaken = Date.now() - startTime;
          setElapsedTime(prev => prev + timeTaken);
          setStartTime(null);
        }

        let correct = 0;
        for (let i = 0; i < currentLine.length; i++) {
          if (inputValue[i] === currentLine[i]) {
            correct++;
          }
        }

        setCorrectChars(prev => prev + correct);
        setTotalChars(prev => prev + currentLine.length);

        if (currentLineIndex < lines.length - 1) {
          setCurrentLineIndex(prev => prev + 1);
          setInputValue("");
          const lastPos = totalTypedChars();
          saveProgress(articleIndex, lastPos);
        } else {
          setCompleted(true);
          saveProgress(articleIndex+1, 0);
        }

      }
    }
  };

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
    
  // useCallback?
  const handleRetry = () => {
    setInputValue("");
    setCompleted(false);
    setCpm(0);
    setCorrectChars(lastPosition);
    setTotalChars(lastPosition);
    setCurrentLineIndex(initialLineIndex);
  };

  useEffect(() => {
    setCurrentLineIndex(0);
    setInputValue("");
    setStartTime(null);
    setElapsedTime(0);
    setCompleted(false);
    setCpm(0);
    setCorrectChars(0);
    setTotalChars(0);
  }, []);

  useEffect(() => {
    handleRetry();
  }, [content]);

  const accuracy = totalChars > 0 ? Math.round((correctChars / totalChars) * 100) : 0;
  const totalLyricsChars = Hangul
    .disassemble(lines.join(""), true)
    .flat().length;

  return (
    <>
      <Wrapper>
        {completed && (
          <ResultModal
            lyrics = {content.split('\n')}
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
                  textDecoration = 'underline';
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

        {nextLines.map((line, idx) => (
          <SubLine key={idx}>{line}</SubLine>
        ))}
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

        <InfoBox>
          <p>평균 {cpm} 타</p>
        </InfoBox>
      </Wrapper>
    </>
  );
};

export default Typing;

const TypingLine = styled.div`
  min-height: 280px;
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


// TypingLocal과 다른 부분
const InfoBox = styled.div`
`;

const SubLine = styled.div`
  margin-bottom: 1rem;
  width: 100%;
  font-size: var(--typing-size);
  color: var(--typing-line-sub);
`;

