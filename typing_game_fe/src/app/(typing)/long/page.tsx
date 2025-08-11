"use client";
import React, { useEffect, useState } from "react";
import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터
import Sidebar from "../../_components/SideBar";
import TypingGame from "../../_components/TypingGame";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";

import styled from "styled-components";
import axios from "axios";


interface LongText {
  longTextId: number;
  title: string;
  content: string;
}

interface Song {
  title: string;
  lyrics: string[];
}

const TypingPage: React.FC = () => {
  const [lyricsList, setLyricsList] = useState<Song[]>([]);
  const [selectedSong, setSelectedSong] = useState<Song | null>(null);
  const [uploadedFiles, setUploadedFiles] = useState<
    { title: string; lyrics: string[] }[]
  >([]);

  const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
    setUploadedFiles((prev) => [...prev, file]);
  };

  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const toggleSidebar = () => setIsSidebarOpen((prev) => !prev);


  // 로그인 상태 확인 (basicAuth)
  const savedAuthHeader = sessionStorage.getItem("authHeader") || "";
  const [authHeader, setAuthHeader] = useState<string>(savedAuthHeader);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(!!savedAuthHeader);

  
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
    headers: { Authorization: authHeader },
    withCredentials: true,
  })
    .then(res => {
      if (res.status === 200) {
        setIsLoggedIn(true);
      }
    })
    .catch(() => {
      setIsLoggedIn(false);
      sessionStorage.removeItem("authHeader");
      setAuthHeader("");
    });
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

// 가사 목록 불러오기
useEffect(() => {
  if (!authHeader) return;

  axios.get("http://localhost:8080/long-text", {
    headers: { Authorization: authHeader },
    withCredentials: true,
  })
    .then(res => {
      const data: LongText[] = res.data.data;
      const songs = data.map(item => ({
        title: item.title,
        lyrics: item.content.split("\n"),
      }));
      setLyricsList(songs);
      if (songs.length > 0) setSelectedSong(songs[0]);
    })
    .catch(err => {
      console.error("API 호출 실패", err);
      if (err.response?.status === 401) {
        setIsLoggedIn(false);
        setAuthHeader("");
        sessionStorage.removeItem("authHeader");
        alert("인증 실패! 다시 로그인 해주세요.");
      }
    });
}, [authHeader]);

if (!selectedSong) {
  return <div>로딩중...</div>;
} 
  return (
    <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={lyricsList}
            uploadedFiles={uploadedFiles}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
            onUploadFile={handleUploadFile}
          />
        </SidebarWrapper>
      )}
      <Content>
        <Header>
          <span>긴글연습</span>
          <Title>{selectedSong.title}</Title>
          <RightInfo>로그인</RightInfo>
        </Header>

        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />

        <MainWrapper>
          <TypingGame lyrics={selectedSong.lyrics} />
        </MainWrapper>
      </Content>
    </Box>
  );
};

export default TypingPage;



const Box = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
`;

const SidebarWrapper = styled.div`
  position: absolute;
  left: 0;
  top: 0;
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  color: black;
  font-size: 0.8rem;
  margin-top: 3rem;
`;

const Title = styled.h1`
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
  text-align: center;
`;

const RightInfo = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  z-index: 1;
`;

const MainWrapper = styled.div`
  width: 600px;
  color: black;
  border-radius: 0 20px 20px 0;
`;
