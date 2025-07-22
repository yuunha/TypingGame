"use client";

import React, { useEffect, useState } from "react";
import "./TypingCat.css"; // 필수 스타일

// 이미지 import

// 타입 지정
interface TypingCatProps {
  isTyping: boolean;
}

// 컴포넌트 구현
const TypingCat: React.FC<TypingCatProps> = ({isTyping }) => {
  const [pawState, setPawState] = useState(false);

  // 타이핑 감지 → 짧게 고양이 애니메이션
  useEffect(() => {
    if (isTyping) {
      setPawState(true);
      const timeout = setTimeout(() => setPawState(false), 100);
      console.log("typing")
      return () => clearTimeout(timeout);
    }
  }, [isTyping]);

  return (
    <div className="container">
      <div className="cat">
        <div
          id="head"
        />
        <div
          id="mouth"
        />
        <div
          id="paw-left"
          style={{
            backgroundPositionX: pawState ? "-630px" : "0px",
          }}
        />
        <div
          id="paw-right"
          style={{
            backgroundPositionX: pawState ? "-800px" : "0px",
          }}
        />
      </div>
    </div>
  );
};

export default TypingCat;
