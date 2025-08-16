"use client";
import React, { useEffect, useState } from "react";
import Sidebar from "../../_components/SideBar";
import TypingGame from "../../_components/TypingGame";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";

import styled from "styled-components";
import axios from "axios";


interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
}


const TypingPage: React.FC = () => {
  const [lyricsList, setLyricsList] = useState<LongText[]>([]);
  const [selectedSong, setSelectedSong] = useState<LongText | null>(null);
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const [authHeader, setAuthHeader] = useState<string>(
    typeof window !== "undefined" ? sessionStorage.getItem("authHeader") || "" : ""
  );
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(!!authHeader);

  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");

  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);


  
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

// 긴글 목록 불러오기
  useEffect(() => {
    if (!isLoggedIn || !authHeader) return;

    const fetchLyrics = async () => {
      try {
        const [allRes, myRes] = await Promise.all([
          axios.get("http://localhost:8080/long-text"),
          axios.get("http://localhost:8080/my-long-text", {
            headers: { Authorization: authHeader },
            withCredentials: true,
          }),
        ]);

        const allLyrics: LongText[] = allRes.data.data.map((item: any) => ({
          longTextId: item.longTextId,
          title: item.title,
          isUserFile: false,
        }));

        const myLyrics: LongText[] = myRes.data.map((item: any) => ({
          longTextId: item.myLongTextId,
          title: item.title,
          isUserFile: true,
        }));

        const combined = [...allLyrics, ...myLyrics];
        setLyricsList(combined);
        if (combined.length > 0) setSelectedSong(combined[0]);
      } catch (err) {
        console.error("긴글 불러오기 실패", err);
      }
    };

    fetchLyrics();
  }, [isLoggedIn, authHeader]);

  return (
    <Box>
      {isSidebarOpen && (
        <SidebarWrapper>
          <Sidebar
            lyricsList={lyricsList}
            selectedSong={selectedSong}
            onSelectSong={setSelectedSong}
          />
        </SidebarWrapper>
      )}
      <Content>
        {selectedSong ? (
          <Header>
            <span> </span>
            <Title>{selectedSong.title}</Title>
            <RightInfo> </RightInfo>
          </Header>
        ) : (<></>)}
        {selectedSong ? (
        <MainWrapper>
            <TypingGame 
            longTextId={selectedSong.longTextId} 
            isLoggedIn={isLoggedIn} 
            isUserFile={selectedSong.isUserFile}/>
        </MainWrapper> 
        ) : (<></>)}
        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />
      </Content>
    </Box>
  );
};

export default TypingPage;



const Box = styled.div`
  position: relative;
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
