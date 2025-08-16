"use client";

import React, { useState, useEffect } from "react";
import styled from "styled-components";
import * as Hangul from "hangul-js";
import ResultModal from "./ResultModal";
import axios from "axios";

interface LongText {
  longTextId: number;
  title: string;
  content: string;
}

interface Song {
  longTextId: number;
  title: string;
  lyrics: string[];
}

interface TypingGameProps {
  longTextId: number;
  isLoggedIn: boolean;
  isUserFile: boolean;
}


const TypingGame: React.FC<TypingGameProps> = ({ longTextId, isLoggedIn, isUserFile }) => {
  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [elapsedTime, setElapsedTime] = useState(0); // 누적 시간
  const [completed, setCompleted] = useState(false);
  const [cpm, setCpm] = useState(0);
  const [correctChars, setCorrectChars] = useState(0);
  const [totalChars, setTotalChars] = useState(0);


  //가사 관련
  const [lyrics, setLyrics] = useState<string[]>([]);

  const currentLine = lyrics[currentLineIndex];
  const p1Line = currentLineIndex + 1 < lyrics.length ? lyrics[currentLineIndex + 1] : null;
  const p2Line = currentLineIndex + 2 < lyrics.length ? lyrics[currentLineIndex + 2] : null;


  // 가사 불러오기
  useEffect(() => {
    if (!isLoggedIn) return;
    if(isUserFile){
      axios.get(`http://localhost:8080/my-long-text/${longTextId}`, {
      withCredentials: true,
    })
      .then(res => {
        const data = res.data;
        setLyrics((data.content || "").split("\n"));
      })
      .catch(err => {
        console.error("가사 불러오기 실패", err);
      })
    }else{
      axios.get(`http://localhost:8080/long-text/${longTextId}`, {
        withCredentials: true,
      })
        .then(res => {
          const data = res.data;
          setLyrics((data.content || "").split("\n"));
        })
        .catch(err => {
          console.error("가사 불러오기 실패", err);
        })
    }
  }, [longTextId, isLoggedIn, isUserFile]);
  
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
            isUserFile = {isUserFile}
          />
        )}
        <ProgressBarContainer>
          <ProgressBarFill progress={totalTypedChars() / totalLyricsChars * 100} />
        </ProgressBarContainer>
      <TypingLine>

        <CurrentLine>
          {(currentLine ?? "").split("").map((char, i) => {
            const typedChar = inputValue[i];
            let color = "var(--default-text)";
            let backgroundColor = "transparent";

            if (typedChar !== undefined) {
              if (typedChar === char) {
                color = "var(--color-correct)";
                backgroundColor = "var(--color-correct-bg)";
              } else {
                color = "var(--color-wrong)";
                backgroundColor = "var(--color-wrong-bg)";
              }
            }

            
            return (
              <CharSpan key={i} style={{ color, backgroundColor }}>
                {char}
              </CharSpan>
            );
          })}
        </CurrentLine>

        {p1Line && <SubLine>{p1Line}</SubLine>}
        {p2Line && <SubLine>{p2Line}</SubLine>}
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
  height : 370px;
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

