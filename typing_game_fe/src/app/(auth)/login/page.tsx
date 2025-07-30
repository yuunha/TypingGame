"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";

import styled from "styled-components";
import Link from 'next/link';


const LoginPage: React.FC = () => {
    const [loginId, setLoginId] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const router = useRouter();

    const handleLogin = async (e: React.FormEvent) => {
      e.preventDefault();
      console.log("로그인 요청 시작..."); 
      try{
        const res = await fetch("http://localhost:8080/user", {
          method: "GET",
        });

        console.log("응답 상태:", res.status);
        console.log("응답 OK?", res.ok);
  
        // if (res.status === 200) {
        //   router.push("/"); // 메인 페이지로 이동
        // }else if (res.status === 401) {
        //   setMessage("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        // }
      }catch(error){
        setMessage("로그인 요청 실패")
      }


    }
    return (
        <>
            <Box>
                <MainWrapper>
                    <Title>로그인</Title>
                    <form onSubmit={handleLogin}>
                    <InputBox>
                    <Input placeholder="아이디" value={loginId} onChange={e => setLoginId(e.target.value)}/>
                    <Input placeholder="비밀번호" value={password} onChange={e => setPassword(e.target.value)} type="password"/>
                    </InputBox>
                    <button type="submit"/>
                    </form>
                    <Link href="/signup" passHref>
                      <White>회원가입</White>
                    </Link>
                  {message && <White>{message}</White>}
                </MainWrapper>

            </Box>
        </>
        
        
      );
    };
    
export default LoginPage;


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
  top:-20px;
`;


const Title = styled.h1`
  font-size: 2.1rem;
  font-weight: bold;
  color:white;
  margin-bottom:20px;
`;

const InputBox = styled.div`
  border: 1px solid #ccc;
  border-radius: 15px;
    padding: 10px;
    background-color:white;
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
    color: white;
`

