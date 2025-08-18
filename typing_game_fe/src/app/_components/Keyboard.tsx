'use client';

import React, { useEffect, useState } from "react";
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
  const [theme, setTheme] = useState<'light' | 'dark'>('light');

  const toggleTheme = () => {
    const next = theme === 'light' ? 'dark' : 'light';
    setTheme(next);
    document.documentElement.setAttribute('data-theme', next);
  };

  useEffect(() => {
    const handleKeyDown = (e) => {
      const key = document.querySelector(`.key--${e.code}`);
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
                if (code === "Backspace") {
                toggleTheme();
                }
            };
          const keyElement = (
            <Key
              key={code}
              className={`key--${code}`}
              $color={color}
              $widthLevel={widthLevel}
              $href={href}
              onClick={handleClick}
            >
              <KeyCap $color={color} $href={href}>{label}</KeyCap>
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
  // display: inline-block;
  // padding : 20px 10px;
  // background-color: var(--keyboard-bg);
  // border-radius : 10px;
  // border : 3px solid var(--keyboard-border);
  // box-shadow:4px 4px 1px -1px #0000001a, 0 2px 4px -2px #0000001a;
`;
const KeyboardWrapper = styled.div`
  display:flex;
  // background-color: var(--keyboard-row-bg);
  // box-shadow: 0 0 2px;
`;



const Key = styled.div<{
  $widthLevel?: number;
  $color?: 'blue' | 'red';
}>`
  height: 55px;
  margin: 2px;
  font-size : 13px;
  border-radius: 5px;
  width: ${({ $widthLevel }) =>
    $widthLevel === 3 ? '120px' :
    $widthLevel === 2 ? '90px' :
    $widthLevel === 1 ? '70px' : '50px'};
  flex-grow: ${({ $widthLevel }) => ($widthLevel === 0 ? 1 : 0)};
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
    color: var(--key-pressed-text);
    background-color: ${({ $href }) => $href ? 'var(--key-linked-pressed)' : '' };
    box-shadow: 0 0 5px 1px ${({ $color }) =>
      $color === 'blue' ? 'var(--key-led-blue)' :
      $color === 'red' ? 'var(--key-led-red)' :
      'var(--key-led-red)'};
     color :  var(--key-led-red);
  }
  &.pressed {
    color: var(--key-pressed-text);
    background-color: ${({ $href }) => $href ? 'var(--key-linked-pressed)' : '' };
    box-shadow: 0 0 5px 1px ${({ $color }) =>
      $color === 'blue' ? 'var(--key-led-blue)' :
      $color === 'red' ? 'var(--key-led-red)' :
      'var(--key-led-red)'};
     color :  var(--key-led-red);
    }
`;

const KeyCap = styled.div<{
  $color?: 'blue' | 'red';
}>`
  width: 99%;
  height: 90%;
  padding: 6px;
  border-radius: 5px;
  line-height: 1;
  background-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-fill-blue)' :
    $color === 'red' ? 'var(--key-fill-red)' :
    'var(--key-fill-default)'};
`;
