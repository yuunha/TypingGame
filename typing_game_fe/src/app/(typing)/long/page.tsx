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
  const toggleSidebar = () => setIsSidebarOpen((prev) => !prev);


  // 로그인 상태 확인 (basicAuth)
  const savedAuthHeader = typeof window !== "undefined" ? sessionStorage.getItem("authHeader") || "" : "";
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
      withCredentials: true,
    })
      .then(res => {
        if (res.status === 200) {
          setIsLoggedIn(true);
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

// 긴글 목록 불러오기
useEffect(() => {
  if (!isLoggedIn) return;
  
  axios.get("http://localhost:8080/long-text", {
  })
    .then(res => {
      const data: LongText[] = res.data.data;
      const songs = data.map(item => ({
        longTextId: item.longTextId,
        title: item.title,
        isUserFile : false,
      }));
      setLyricsList(songs)
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

    // 사용자 파일
    axios.get("http://localhost:8080/my-long-text", {
      withCredentials: true,
    })
      .then(res => {
        const data: LongText[] = res.data;
        const songs = data.map(item => ({
          longTextId: item.myLongTextId,
          title: item.title,
          isUserFile : true,
        }));
        setLyricsList(prev => [...prev, ...songs])
      }).catch(error =>{
        console.log(error)
      })
}, [isLoggedIn]);


// 전체 유저의 긴글점수 목록 조회
// useEffect(() => {
//   axios.get("http://localhost:8080/user/long-text/scores", {
//     withCredentials: true,
//   })
//     .then(res => {
//       console.log('전체 목록', res.data)
//     })
//     .catch(err => {
//       console.error("API 호출 실패", err);
//     });
// }, []);


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
        <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />

        {selectedSong ? (
        <MainWrapper>
            <TypingGame longTextId={selectedSong.longTextId} isLoggedIn={isLoggedIn} isUserFile={selectedSong.isUserFile}/>
         
        </MainWrapper> 
        ) : (<></>)}
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
