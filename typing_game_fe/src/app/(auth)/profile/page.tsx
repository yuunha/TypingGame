"use client";
import React, { useEffect, useState } from "react";
import miniKeys from "../../_components/keyboard/miniKeys";

import styled from "styled-components";
import axios from "axios";
import KeyboardMini from "@/app/_components/KeyboardMini";



const Profile: React.FC = () => {
  // 로그인 상태 확인 (basicAuth)
  const savedAuthHeader = typeof window !== "undefined" ? sessionStorage.getItem("authHeader") || "" : "";
  const [authHeader, setAuthHeader] = useState<string>(savedAuthHeader);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(!!savedAuthHeader);

  const [username, setUsername] = useState('');
  const [userId, setUserId] = useState('');
  
  // const handleLogout = () => {
  //   sessionStorage.removeItem("authHeader");
  //   setAuthHeader("");
  //   setIsLoggedIn(false);
  // };


  
  // 로그인 유지 확인 (로그인 상태 조회)
  useEffect(() => {
    if (!authHeader) {
      setIsLoggedIn(false);
      return;
    }

    axios.get("http://localhost:8080/user", {
      withCredentials: true,
    })
      .then(res => {
        if (res.status === 200) {
          setIsLoggedIn(true);
          setUsername(res.data.username);
          setUserId(res.data.userId);
          console.log("로그인 되어 있습니다")
        }
      })
      
  }, [authHeader]);

  // 로그인 안 되어 있으면 prompt 띄우기
  useEffect(() => {
    if (!isLoggedIn) {
      const username = prompt("아이디를 입력하세요") || "";
      const password = prompt("비밀번호를 입력하세요") || "";
      if (username && password) {
        const basicAuth = "Basic " + btoa(`${username}:${password}`);
        sessionStorage.setItem("authHeader", basicAuth);
        setAuthHeader(basicAuth);
        setIsLoggedIn(true);
      } else {
        alert("로그인 정보가 필요합니다.");
      }
    }
  }, [isLoggedIn]);

  // 회원정보 수정
  const handleUpdateProfile = () => {
    axios.put(`http://localhost:8080/user/${userId}`, 
      { username },
      { withCredentials: true, 
        headers: { Authorization: authHeader },})
    .then(res => {
      console.log("회원정보가 수정되었습니다.");
    })
    .catch(err => {
      console.log("실패", err);
    })
  };


  return (
    <Box>
      <Content>
        <KeyboardMini keys={miniKeys} />

        <MainWrapper>
         <h2>회원 정보</h2>
          <div>
            <label>닉네임</label>
            <input 
              value={username}
              onChange={e => setUsername(e.target.value)}
            />
          </div>
          <button onClick={handleUpdateProfile}>회원정보수정</button>
        </MainWrapper> 
      </Content>
    </Box>
  );
};

export default Profile;



const Box = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
`;


const Content = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;


const MainWrapper = styled.div`
  width: 600px;
  color: black;
  border-radius: 0 20px 20px 0;
`;
