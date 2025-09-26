"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import styled from "styled-components";
import { Keys } from "@/types/key-item";

interface KeyboardProps {
  keys: Keys;
  onToggleSidebar?: () => void;
}

export default function KeyboardClient({ keys, onToggleSidebar }: KeyboardProps) {
  const [theme, setTheme] = useState<"light" | "dark">("light");

  const toggleTheme = () => {
    const next = theme === "light" ? "dark" : "light";
    setTheme(next);
    document.documentElement.setAttribute("data-theme", next);
  };

  useEffect(() => {
      const handleKeyDown = (e: KeyboardEvent) => {
        const key = document.querySelector(`.key--${e.code}`);
        if (key) key.classList.add("pressed");
      };
  
      const handleKeyUp = (e: KeyboardEvent) => {
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
              if (code === "CapsLock") onToggleSidebar?.();
              if (code === "Backspace") toggleTheme();
            };

            const keyElement = (
              <Key
                key={code}
                className={`key--${code}`}
                $color={color}
                $widthLevel={widthLevel}
                onClick={handleClick}
              >
                <KeyCap>{label}</KeyCap>
              </Key>
            );

            return href ? (
              <Link href={href} key={code} passHref>
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
}

const KeyboardFrame = styled.div`
  width:100%;
`;
const KeyboardWrapper = styled.div`
  display:flex;
  width:100%;
`;



const Key = styled.div<{
  $widthLevel?: number;
  $color?: 'blue' | 'red';
}>`
  height: 55px;
  margin: 2px;
  font-size : 13px;
  border-radius: 4px;
  display:flex;
  justify-content:center;
  align-items: flex-start;

  width: ${({ $widthLevel }) =>
    $widthLevel === 3 ? '120px' :
    $widthLevel === 2 ? '90px' :
    $widthLevel === 1 ? '70px' : '50px'};
  flex-grow: ${({ $widthLevel }) => ($widthLevel === 0 ? 1 : 0)};
  border-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-border-blue)' :
    $color === 'red' ? 'var(--key-border-red)' :
    'var(--key-border-default)'};
  border : 1px solid black;
  background-color: ${({ $color }) =>
    $color === 'blue' ? 'var(--key-fill-blue)' :
    $color === 'red' ? 'var(--key-fill-red)' :
    'var(--key-fill-default)'};
  color: ${({ $color }) => ($color ? 'white' : 'black')};
  &:hover{
    color: var(--key-pressed-text);
    background-color: var(--key-linked-pressed);
    box-shadow: 0 0 5px 1px ${({ $color }) =>
      $color === 'blue' ? 'var(--key-led-blue)' :
      $color === 'red' ? 'var(--key-led-red)' :
      'var(--key-led-red)'};
     color :  var(--key-led-red);
  }
  &.pressed {
    color: var(--key-pressed-text);
    background-color: var(--key-linked-pressed);
    box-shadow: 0 0 5px 1px ${({ $color }) =>
      $color === 'blue' ? 'var(--key-led-blue)' :
      $color === 'red' ? 'var(--key-led-red)' :
      'var(--key-led-red)'};
     color :  var(--key-led-red);
    }
`;

const KeyCap = styled.div`
  width: 99%;
  height: 90%;
  padding: 6px;
  line-height: 1;
`;
