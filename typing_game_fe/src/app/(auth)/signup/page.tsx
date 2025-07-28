"use client";
import React, { useState } from "react";

import styled from "styled-components";

const SignupPage: React.FC = () => {
    
  const [username, setUsername] = useState("");
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault(); 
        console.log({ username, id, password });
    };

    return (
        <>
            <Box>
                <MainWrapper>
                    <Title>회원가입</Title>
                    <form onSubmit={handleSubmit}>
                    <InputBox>
                    <Input placeholder="닉네임" value={username} onChange={e => setUsername(e.target.value)}/>
                    <Input placeholder="아이디" value={id} onChange={e => setId(e.target.value)}/>
                    <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
                    </InputBox>
                    <button type="submit"/>
                    </form>                    
                </MainWrapper>

            </Box>
        </>
    )
}
export default SignupPage;



const Box = styled.div`
  flex-direction: column;  // 수직 정렬 추가
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
  z-index: 1;
`;

const MainWrapper = styled.div`
  width: 280px;
  color: black;
  display: flex;
  flex-direction: column;
  align-items: center;
  position:relative;
  top:-40px;
`;


const Title = styled.h1`
  font-size: 2.1rem;
  font-weight: bold;
  color:white;
  margin-bottom:20px;
`;

const InputBox = styled.div`
  border: 1px solid #ccc;
  border-radius: 20px;
    padding: 10px;
    background-color:white;
    font-size: 14px;
  ::placeholder {
    color: #6b6b6bd0;
    font-size: 14px;
  }
`

const Input = styled.input`
    padding: 8px;
    width:100%;
    border:none;
    outline:none;
    &:not(:last-child) {
        border-bottom: 0.1rem solid #dadada7a;
    }
    &:hover {
    // background: rgba(255, 255, 255, 0.1);

    }
    
`
