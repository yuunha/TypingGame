"use client";

import React, { useState } from "react";

import styled from "styled-components";
import { useAuth } from "@/app/hooks/useAuth";

const LoginPage: React.FC = () => {
  const { promptLogin } = useAuth();

  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");
    
  return (
    <Content>
      <CardsWrapper>
        <Card>
          <Title>계정 접속하기</Title>
          <form onSubmit={promptLogin}>
            <InputBox>
            <Input placeholder="아이디" value={loginId} onChange={e => setLoginId(e.target.value)}/>
            <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
            </InputBox>
            <SubmitButton type="submit">Login</SubmitButton>
          </form>
          <SignHref><a href="/signup">새 계정 만들기</a></SignHref>
        </Card>   
      </CardsWrapper>
    </Content>
  );
};
    
export default LoginPage;


const Content = styled.div`
  display: flex;
  flex-direction: column;
  padding-top : 200px;
`;

const CardsWrapper = styled.div`
  width: 300px;
  display: flex;
  align-items: flex-start;
  gap: 20px;
`;

const Card = styled.div`
  flex: 1;
  min-width: 0;
`


const Title = styled.h1`
  margin-bottom:20px;
  text-align: center;
  font-size: 1.5rem;
`;

const SignHref = styled.div`
text-align: right;
color : var(--typing-line-sub);
  a {
    font-size: 0.8rem;
  }
`

const InputBox = styled.div`
  border: 1px solid #ccc;
  border-radius: 15px;
    padding: 10px;
    font-size: 14px;
  ::placeholder {
    color: #6b6b6bd0;
    font-size: 14px;
  }
`

const Input = styled.input`
    padding: 6px;
    width:100%;
    border:none;
    outline:none;
    &:not(:last-child) {
        border-bottom: 0.1rem solid #dadada7a;
    }
`
const SubmitButton = styled.button`
  margin-top: 20px;
  padding: 4px 0;
  border-radius: 9px;
  border: 1px solid black;
  cursor: pointer;
  transition: background-color 0.2s;
    width:100%;
  &:hover {
    background-color: var(--key-fill-red);
  }
`;
