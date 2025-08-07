'use client';

import React, { useEffect } from "react";
import styled from "styled-components";
import Link from 'next/link';
import '../globals.css';

interface KeyItem {
  code: string;
  label: string;
  color?: string;
  widthLevel?: number;
  href?: string;
}

type Keys = KeyItem[][];

interface KeyboardProps {
  keys: Keys;
  onToggleSidebar?: () => void;
}

const Keyboard: React.FC<KeyboardProps> = ({ keys, onToggleSidebar }) => {
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
    <KeyboardFrame>
      {keys.map((row, rowIndex) => (
        <KeyboardWrapper key={rowIndex}>
          {row.map(({ code, label, color, widthLevel, href }) => {
            const handleClick = () => {
                if (code === "CapsLock") {
                onToggleSidebar?.();
                }
            };
          const keyElement = (
            <Key
              key={code}
              className={`key--${code}`}
              $color={color}
              $widthLevel={widthLevel}
              onClick={handleClick}
            >
              <KeyCap $color={color}>{label}</KeyCap>
            </Key>
          );

          return href ? (
            <Link href={href} passHref key={code}>
              {keyElement}
            </Link>
          ) : (
            keyElement
          );
        })}
        </KeyboardWrapper>
      ))}
    </KeyboardFrame>
  );
};

export default Keyboard;

const KeyboardFrame = styled.div`
  display: inline-block;
  padding : 20px 10px;
  background-color: var(--keyboard-bg);
  box-shadow: 0 0 10px;
  border-radius : 10px;
`;
const KeyboardWrapper = styled.div`
  display:flex;
  background-color: var(--keyboard-row-bg);
  box-shadow: 0 0 2px;
`;



const Key = styled.div<{
  $widthLevel?: number;
  $color?: 'blue' | 'red';
}>`
  width: ${({ $widthLevel }) =>
    $widthLevel === 3 ? '120px' :
    $widthLevel === 2 ? '90px' :
    $widthLevel === 1 ? '70px' : '50px'};
  flex-grow: ${({ $widthLevel }) => ($widthLevel === 0 ? 1 : 0)};
  height: 55px;
  margin: 2px;
  border: 7px solid;
  border-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-border-blue)' :
    $color === 'red' ? 'var(--key-border-red)' :
    'var(--key-border-default)'};
  background-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-fill-blue)' :
    $color === 'red' ? 'var(--key-fill-red)' :
    'var(--key-fill-default)'};
  color: ${({ $color }) => ($color ? 'white' : 'black')};
  &:hover{
    transform: scale(0.95);
    border-color: var(--key-pressed-border);
    color: var(--key-pressed-text);
  }
  transition: transform 0.05s ease, box-shadow 0.05s ease;
  box-shadow: 0 3px 5px rgba(0, 0, 0, 0.2);
  &.pressed {
    transform: scale(0.95) translateY(1px);
    box-shadow: 0 1px 2px rgba(0,0,0,0.1);
    animation: press 0.15s ease;
    border-color: var(--key-pressed-border);
    color: var(--key-pressed-text);
  }
`;

const KeyCap = styled.div<{
  $color?: 'blue' | 'red';
}>`
  width: 100%;
  height: 41px;
  font-size: 13px;
  padding: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,0.2);
  border-radius: 5px;
  line-height: 1;
  background-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-fill-blue)' :
    $color === 'red' ? 'var(--key-fill-red)' :
    'var(--key-fill-default)'};
`;
