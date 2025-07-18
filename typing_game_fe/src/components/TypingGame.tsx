"use client";

import React, { useState, useEffect } from "react";

interface TypingGameProps {
    lyrics: string[];
}

const TypingGame: React.FC<TypingGameProps>  = ({ lyrics }) => {
  const [currentLineIndex, setCurrentLineIndex] = useState(0);
  const [inputValue, setInputValue] = useState("");
  const [startTime, setStartTime] = useState<number | null>(null);
  const [completed, setCompleted] = useState(false);

  const m2Line =
    currentLineIndex > 1 ? lyrics[currentLineIndex-2] : null;
  const m1Line =
    currentLineIndex > 0 ? lyrics[currentLineIndex-1] : null;
  const currentLine = lyrics[currentLineIndex]; 
  const p1Line = 
    currentLineIndex < lyrics.length ? lyrics[currentLineIndex+1]:null; 
  /**
     * currentLine : 현재 보여주는 텍스트 (lyrics[0]...)
     * currentLineIndex : 현재 줄의 타이핑이 정답인 경우 +1
     */

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

  const totalTypedChars = lyrics
    .slice(0, currentLineIndex)
    .join("")
    .length + inputValue.length;
  /**
     * lyrics.slice(0,currenLineINdex) : 현재 줄 이전의 모든 줄을 잘라서 새 배열로 만듦
     * join -> 잘라낸 배열을 하나의 문자열로 합침 ["동해물과","백두산이"].join("")="동해물과백두산이"
     */
    const cpm = startTime
    ? Math.round(totalTypedChars / ((Date.now() - startTime) / 60000))
    : 0;



  if (completed) {
    return (
      <div className="flex flex-col items-center justify-center mt-20 text-center">
        <h2 className="text-3xl font-bold">🎉 완료!</h2>
        <p className="mt-4 text-xl">속도: {cpm}타</p>
      </div>
    );
  }

  return (
    <div className="flex flex-col items-center mt-20">
      <h1 className="text-2xl font-bold mb-6">🇰🇷 애국가 타자 연습</h1>
      <p className="text-xl mb-4 text-gray-400">{m1Line}</p>
      <p className="text-xl mb-4 text-gray-400">{m2Line}</p>
      <p className="text-xl mb-4">
        {currentLine.split("").map((char,i)=>{
           const typedChar = inputValue[i];
           let colorClass = "";

           if(typedChar !== undefined){
                if (i === inputValue.length - 1) {
                    colorClass = "text-black"; // 기본색
                } else{
                    colorClass = typedChar == char ? "text-blue-500": "text-red-500";
                } 
            }
           return (
            <span key={i} className={colorClass}>
                {char}
            </span>
           );
        })}
      </p>
      <p className="text-xl mb-4 text-gray-400">{p1Line}</p>
      <input
        type="text"
        value={inputValue}
        onChange={handleChange}
        placeholder="여기에 입력하세요"
        className="border rounded-lg px-4 py-2 text-lg w-250 focus:outline-none focus:ring-2 focus:ring-blue-400"
      />
      <div className="mt-4 flex gap-4 text-lg">
        <p>속도: {cpm} 타 </p>
        <p>
          진행률: {currentLineIndex + 1}/{lyrics.length}
        </p>
      </div>
    </div>
  );
};
export default TypingGame;
