"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";

import styled from "styled-components";
import axios from "axios";
import KeyboardMini from "@/app/_components/KeyboardMini";
import authKeys from "../../_components/keyboard/authKeys";

const SignupPage: React.FC = () => {
    
  const [username, setUsername] = useState("");
  const [loginId, setId] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const router = useRouter();


  const handleSignup = async (e: React.FormEvent) => {
    e.preventDefault();
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    axios.post(`${baseUrl}/user`, 
      { username,
        loginId,
        password,
       },
      { withCredentials: true,})
    .then(res => {
      console.log("회원가입 완료");
      router.push("/login");
    })
    .catch(err => {
      console.log("실패", err);
    })
  };


    return (
      <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
        <MainWrapper>
          <Title>회원가입</Title>
          <form onSubmit={handleSignup}>
          <InputBox>
          <Input placeholder="닉네임" value={username} onChange={e => setUsername(e.target.value)}/>
          <Input placeholder="아이디" value={loginId} onChange={e => setId(e.target.value)}/>
          <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
          </InputBox>
          <button type="submit"/>
          </form>                    
      </MainWrapper>
      </Content>
    </Box>
    )
}
export default SignupPage;



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
  border-radius: 20px;
    padding: 10px;
    background-color:var(--keyboard-bg);
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
