'use client';

import React, { useEffect } from "react";
import styled from "styled-components";

export default function KeyBoard() {
  useEffect(() => {
  const handleKeyDown = (e) => {
  const key = document.querySelector(`.key--${e.keyCode}`);
  console.log("keyCode:", e.keyCode); // 숫자 (구식)
  console.log("key:", e.key);         // 실제 눌린 문자나 키 이름 ('a', 'Enter', '<' 등)
  console.log("code:", e.code);       // 물리적인 키 위치 기준 ('KeyA', 'Enter' 등)
  if (key) key.classList.add("pressed");
};

  const handleKeyUp = (e) => {
    const key = document.querySelector(`.key--${e.keyCode}`);
    if (key) key.classList.remove("pressed");
  };

  document.addEventListener("keydown", handleKeyDown);
  document.addEventListener("keyup", handleKeyUp);

  return () => {
    document.removeEventListener("keydown", handleKeyDown);
    document.removeEventListener("keyup", handleKeyUp);
  };
}, []);
  return (
    <>
      <Keyboard>
        <KeyboardWrapper>
          <Key className="key--27" $isRed><KeyCap $isRed>Esc</KeyCap></Key>
          <Key className="key--81"><KeyCap>☀</KeyCap></Key>
          <Key className="key--87"><KeyCap>☁</KeyCap></Key>
          <Key className="key--69"><KeyCap>사</KeyCap></Key>
          <Key className="key--82"><KeyCap>이</KeyCap></Key>
          <Key className="key--85"><KeyCap>트</KeyCap></Key>
          <Key className="key--73"><KeyCap>☂</KeyCap></Key>
          <Key className="key--79"><KeyCap>☃</KeyCap></Key>
          <Key className="key--80"><KeyCap>♧</KeyCap></Key>
          <Key className="key--36" $isColor $width2><KeyCap $isColor>Home</KeyCap></Key>
        </KeyboardWrapper>
        <KeyboardWrapper>
          <Key className="key--20" $isColor $width2><KeyCap $isColor>Caps</KeyCap></Key>
            <Key className="key--65"><KeyCap>긴</KeyCap></Key>
            <Key className="key--83"><KeyCap>짧</KeyCap></Key>
            <Key className="key--68"><KeyCap>낱</KeyCap></Key>
            <Key className="key--70"><KeyCap>랭✧</KeyCap></Key>
          <Key className="key--74"><KeyCap>🜸</KeyCap></Key>
          <Key className="key--75"><KeyCap>𐂂</KeyCap></Key>
          <Key className="key--76"><KeyCap>㋛</KeyCap></Key>
          <Key className="key--8" $width0><KeyCap>Backspace⌫</KeyCap></Key>
        </KeyboardWrapper>
        <KeyboardWrapper>
          <Key className="key--16" $isColor $width3><KeyCap $isColor>⇧ Shift</KeyCap></Key>
          <Key className="key--90"><KeyCap></KeyCap></Key>
          <Key className="key--88"><KeyCap></KeyCap></Key>
          <Key className="key--67"><KeyCap></KeyCap></Key>
          <Key className="key--86"><KeyCap>☾</KeyCap></Key>
          <Key className="key--86"><KeyCap>❤︎</KeyCap></Key>
          <Key className="key--78"><KeyCap>?</KeyCap></Key>
          <Key className="key--13" $isRed $width0><KeyCap $isRed>⏎ Enter</KeyCap>
          </Key>
          </KeyboardWrapper>
          <KeyboardWrapper>
          <Key className="key--17" $isColor $width1><KeyCap $isColor>Ctrl</KeyCap></Key>
          <Key className="key--91" $isColor><KeyCap $isColor>🪟</KeyCap></Key>
          <Key className="key--18" $isColor><KeyCap $isColor>Alt</KeyCap></Key>
          <Key className="key--32 " $width0><KeyCap></KeyCap></Key>
          <Key className="key--21" $isColor $width1><KeyCap $isColor>한/영</KeyCap></Key>
          <Key className="key--37" $isColor><KeyCap $isColor>{"<"}</KeyCap></Key>
          <Key className="key--39" $isColor ><KeyCap $isColor>{">"}</KeyCap></Key>
        </KeyboardWrapper>
      </Keyboard>
    </>
  );
}

const Keyboard = styled.div`
  display: inline-block;
  padding : 20px 10px;
  background-color : #eeece8ff;
  box-shadow: 0 0 10px rgb(0,0,0);
  border-radius : 10px;
`;

const KeyboardWrapper = styled.div`
  display:flex;
  background-color : #493823ff;
  box-shadow: 0 0 2px rgb(0,0,0);
`;



const Key = styled.div`
  width: ${(props) => 
    props.$width3 ? "120px" : 
    props.$width2 ? "90px" : 
    props.$width1 ? "70px" :
    props.$width0 ? "auto" :
    "50px"
  };
  flex-grow: ${(props) => (props.$width0 ? 1 : 0)};
  height: 55px;
  margin: 2px;
  border: 7px solid;
  border-color: ${(props) => (
    props.$isColor ? "#648da7ff #3a6986ff" : 
    props.$isRed ? "#f29288  #af594fff" :
    "#e4e2dbff #ccc4b5ff")};
  background-color: ${(props) => (
    props.$isColor ? "#7ea8c2ff" : 
    props.$isRed ? "#E2675A":
    "#eeebe1ff")};
  color: ${(props) => (props.$isColor || props.$isRed ? "white" : "")};
  &:hover{
    transform: scale(0.95);
  }
  transition: transform 0.05s ease, box-shadow 0.05s ease;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.2);
  &.pressed {
    transform: scale(0.95) translateY(1px);
    box-shadow: 0 1px 2px rgba(0,0,0,0.1);
    animation: press 0.15s ease;
  }
`;

const KeyCap = styled.div`
  width : 100%;
  height: 41px;
  font-size : 13px;
  padding: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,0.2);
  border-radius : 5px;
  line-height: 1;
  background-color: ${(props) => (
    props.$isColor ? "#7ea8c2ff" :
    props.$isRed ? "#E2675A":
    "#eeebe1ff")};
`;
