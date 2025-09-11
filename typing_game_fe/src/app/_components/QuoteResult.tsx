"use client";

import React, { useEffect, useState } from "react";
import ReactDOM from "react-dom";
import styled from "styled-components";
import html2canvas from "html2canvas";
import { FiDownload, FiX } from "react-icons/fi";


interface ResultModalProps {
  lyrics: string;
  accuracy: number;
  cpm: number;
  onRetry: () => void;
}

const ResultModal: React.FC<ResultModalProps> = ({
  lyrics,
  accuracy,
  cpm,
  onRetry,
}) => {
  const [mounted, setMounted] = useState(false);
  const today = new Date();   
  const year = today.getFullYear();
  const month = today.getMonth() + 1; 
  const date = today.getDate(); 

  const saveAsImage = async () => {
    const element = document.getElementById("result");
    if (!element) return;
    const canvas = await html2canvas(element, {
      backgroundColor: "#fff",
      scale: 2, 
    });
    const link = document.createElement("a");
    link.href = canvas.toDataURL("image/png");
    link.download = "result.png";
    link.click();
  };

  useEffect(() => {
    setMounted(true);
    return () => setMounted(false);
  }, []);
  
  if (!mounted) return null;

  return ReactDOM.createPortal(
    <Backdrop>
      <ModalBox id="result">
        <ButtonWrapper>
          <Button onClick={saveAsImage}><FiDownload /></Button>
          <Button onClick={onRetry}><FiX /></Button>
        </ButtonWrapper>
        <NavBarLogo> TYLE</NavBarLogo>
        <p>{lyrics}</p>
        <p>{year}/{month}/{date}</p>
        <ResultStats>
          <StatBox>정확도 {accuracy}%</StatBox>
          <StatBox>평균 {cpm}타</StatBox>
        </ResultStats>
      </ModalBox>
    </Backdrop>,
    document.body
  );
};

export default ResultModal;
const ButtonWrapper = styled.div`
  display:flex;
  gap : 10px;
  justify-content: right;
  align-items: center;
  color: #292929ff;
`
const Button = styled.button`
  font-size: 1rem;
  cursor: pointer;
`

const NavBarLogo = styled.div`
    font-family: Libertinus, Helvetica, sans-serif;
    font-weight : bold;
`;

const Backdrop = styled.div`
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9;
`;

const ModalBox = styled.div`
position: relative;
  background: #ffffff;
  width : 310px;
  padding: 1.3rem 2rem 2rem 2rem;
  border-radius: 4px;
  max-width: 380px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.6);
  animation: fadeIn 0.3s ease-out;

  h2 {
    margin-top: 0.8rem;
    margin-bottom: 0.3rem;
    font-size: 1rem;
    font-weight: 600;
  }

  p {
    font-size: 0.9rem;
    // color: #bbb;
    margin: 0.3rem 0;
  }

  @keyframes fadeIn {
    from {
      transform: translateY(20px);
      opacity: 0;
    }
    to {
      transform: translateY(0px);
      opacity: 1;
    }
  }
`;

const ResultStats = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  justify-content: left;
`;

const StatBox = styled.div`
  font-size: 0.85rem;
  font-weight: 500;
`;

