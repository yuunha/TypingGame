"use client";
import React, { useState } from "react";
import { useRouter } from "next/navigation";

import styled from "styled-components";

const SignupPage: React.FC = () => {
    
  const [username, setUsername] = useState("");
  const [loginId, setId] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const router = useRouter();

  const handleSignup = async (e: React.FormEvent) => {
      e.preventDefault(); 
      console.log("회원가입 요청 시작..."); 
      console.log({ username, loginId, password });
      try{
        const res = await fetch("http://localhost:8080/user", {
          method: "POST",
          headers: {
            "Content-Type": "application/json", // JSON 타입 지정
          },
          body: JSON.stringify({
            username,
            loginId, 
            password,
          }),
        });

        console.log("응답 상태:", res.status);
        console.log("응답 OK?", res.ok);
  
        const text = await res.text();
        setMessage(text);
        if (res.status === 201) {
          router.push("/"); // 메인 페이지로 이동
        }else if (res.status === 409) {
          setMessage("회원가입 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
      }catch(error){
        setMessage("회원가입 요청 실패")
      }
    }

    return (
        <>
            <Box>
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
