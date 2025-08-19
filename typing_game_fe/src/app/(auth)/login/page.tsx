"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

import styled from "styled-components";
import KeyboardMini from "@/app/_components/KeyboardMini";
import authKeys from "../../_components/keyboard/authKeys";
import { useAuth } from "@/app/hooks/useAuth";

const LoginPage: React.FC = () => {
    const [loginId, setLoginId] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const router = useRouter();
    const { promptLogin } = useAuth();

    const handleLogin = async (e: React.FormEvent) => {
      e.preventDefault();
      console.log("로그인 요청 시작..."); 
      try{
        const baseUrl = process.env.NEXT_PUBLIC_API_URL;
        const res = await fetch(`${baseUrl}/user`, {
          method: "GET",
        });

        console.log("응답 상태:", res.status);
        console.log("응답 OK?", res.ok);
  
        const text = await res.text();
        setMessage(text);
        if (res.status === 200) {
          router.push("/"); // 메인 페이지로 이동
        }else if (res.status === 401) {
          setMessage("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
      }catch(error){
        setMessage("로그인 요청 실패")
      }
    }
    return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
        <MainWrapper>
          <Title>로그인</Title>
          <form onSubmit={promptLogin}>
          <InputBox>
          <Input placeholder="아이디" value={loginId} onChange={e => setLoginId(e.target.value)}/>
          <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
          </InputBox>
          <button type="submit"/>
          </form>
        {message && <White>{message}</White>}
      </MainWrapper>
      </Content>
    </Box>
      );
    };
    
export default LoginPage;

const Box = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background: var(--background);
`;


const Content = styled.div`
  margin-top : 100px;
  flex-direction: column;
  align-items: center;
  display:flex;
`;

const MainWrapper = styled.div`
  width: 280px;
  color: black;
  display: flex;
  flex-direction: column;
  align-items: center;
  position:relative;
  margin-top : 100px;
`;


const Title = styled.h1`
  font-size: 2.1rem;
  font-weight: bold;
  color:var(--auth-beigie-title);
  margin-bottom:20px;
`;

const InputBox = styled.div`
  border: 1px solid #ccc;
  border-radius: 15px;
    padding: 10px;
    background-color:var(--keyboard-bg);
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
    &:hover {
    // background: rgba(255, 255, 255, 0.1);

    }    
`
const White = styled.p`
    color: var(--auth-beigie-title);
`

