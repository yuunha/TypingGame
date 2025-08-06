'use client';

import React, { useEffect } from "react";
import styled from "styled-components";
import keys from './keyboard/typingKeys'

export default function KeyBoard() {
  useEffect(() => {
    const handleKeyDown = (e) => {
      const key = document.querySelector(`.key--${e.code}`);
      console.log(e.code)
      if (key) key.classList.add("pressed");
    };

    const handleKeyUp = (e) => {
      const key = document.querySelector(`.key--${e.code}`);
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
        {keys.map((row, rowIndex) => (
        <KeyboardWrapper key={rowIndex}>
          {row.map(({ code, label, color, red, width }) => (
            <Key
              key={code}
              className={`key--${code}`}
              $isColor={color}
              $isRed={red}
              $width0={width === 0}
              $width1={width === 1}
              $width2={width === 2}
              $width3={width === 3}
            >
              <KeyCap $isColor={color} $isRed={red}>{label}</KeyCap>
            </Key>
          ))}
        </KeyboardWrapper>
      ))}
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
