"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

import styled from "styled-components";
import axios from "axios";

const SignupPage: React.FC = () => {
  const router = useRouter();

  const [message, setMessage] = useState("");
  const [username, setUsername] = useState("");
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
    
  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    try {
      await axios.post(
        `${baseUrl}/user`,
        { username, loginId : id, password },
        { withCredentials: true }
      );
      router.push("/login");
    } catch (err) {
      setMessage("회원가입 실패: 서버 에러");
      console.log(err)
    }
  };

  return (
    <Content>
      <CardsWrapper>
        <Card>
          <Title>새 계정 만들기</Title>
          <form onSubmit={handleSignup}>
            <InputBox>
            <Input placeholder="닉네임" value={username} onChange={e => setUsername(e.target.value)}/>
            <Input placeholder="아이디" value={id} onChange={e => setId(e.target.value)}/>
            <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
            </InputBox>
            <SubmitButton type="submit">Sign</SubmitButton>
          </form>
          {message && <White>{message}</White>}               
        </Card>     
      </CardsWrapper>
    </Content>
  );
};
    
export default SignupPage;


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
  background:inherit;
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

const White = styled.p`
    color: var(--auth-beigie-title);
`