"use client";
import React, { useEffect, useState } from "react";
import authKeys from "../../_components/keyboard/authKeys";

import styled from "styled-components";
import axios from "axios";
import KeyboardMini from "@/app/_components/KeyboardMini";
import { useRouter } from "next/navigation";



const Profile: React.FC = () => {
  // 로그인 상태 확인 (basicAuth)
  const [authHeader, setAuthHeader] = useState<string>(
    typeof window !== "undefined" ? sessionStorage.getItem("authHeader") || "" : ""
  );
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(!!authHeader);

  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");
  
  const router = useRouter();
  
  const handleLogout = () => {
    sessionStorage.removeItem("authHeader");
    setAuthHeader("");
    setIsLoggedIn(false);
  };


  
  // 로그인 유지 확인 (로그인 상태 조회)
  useEffect(() => {
    if (!authHeader) {
      setIsLoggedIn(false);
      return;
    }

    const checkLogin = async () => {
      try {
        const res = await axios.get("http://localhost:8080/user", {
          headers: { Authorization: authHeader },
          withCredentials: true,
        });
        if (res.status === 200) {
          console.log("로그인 성공 ", res.data)
          setIsLoggedIn(true);
          setUsername(res.data.username);
          setUserId(res.data.userId);
        }
      } catch (err) {
        console.error("로그인 실패", err);
        sessionStorage.removeItem("authHeader");
        setAuthHeader("");
        setIsLoggedIn(false);
      }
    };

    checkLogin();
  }, [authHeader]);

  // 로그인 안 되어 있으면 prompt 띄우기
  useEffect(() => {
      if (!isLoggedIn) {
        const login = async () => {
          const usernameInput = prompt("아이디를 입력하세요") || "";
          const passwordInput = prompt("비밀번호를 입력하세요") || "";
          if (!usernameInput || !passwordInput) {
            alert("로그인 정보가 필요합니다.");
            return;
          }
  
          const basicAuth = "Basic " + btoa(`${usernameInput}:${passwordInput}`);
          sessionStorage.setItem("authHeader", basicAuth);
          setAuthHeader(basicAuth);
        };
  
        login();
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

  const handleDeleteProfile = () => {
    axios.delete("http://localhost:8080/user", {
      withCredentials: true,
        headers: {
        Authorization: authHeader,
      },
    }).then(res => {
        if (res.status === 200) {
          sessionStorage.removeItem("authHeader");
          setIsLoggedIn(false);
          console.log("탈퇴 성공")
        }
      }).catch(err => {
      console.error("회원 탈퇴 실패", err);
    });

  }

  return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />

        <MainWrapper>
         <h2>회원 정보</h2>
          <div>
            <label>닉네임</label>
            <input 
              value={username}
              onChange={e => setUsername(e.target.value)}
            />
          </div>
          <button onClick={handleLogout}>로그아웃</button>
          <button onClick={handleUpdateProfile}>회원정보수정</button>
          <button onClick={handleDeleteProfile}>탈퇴하기</button>
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
