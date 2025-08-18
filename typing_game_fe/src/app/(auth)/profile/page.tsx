"use client";
import React, { useEffect, useState } from "react";

import styled from "styled-components";
import axios from "axios";
import { useRouter } from "next/navigation";
import KeyboardMini from "@/app/_components/KeyboardMini";
import authKeys from "../../_components/keyboard/authKeys";
import { text } from "stream/consumers";


interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
  score?: number;
}

const Profile: React.FC = () => {
  // 로그인 상태 확인 (basicAuth)
  const [authHeader, setAuthHeader] = useState<string>(
    typeof window !== "undefined" ? sessionStorage.getItem("authHeader") || "" : ""
  );
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(!!authHeader);

  const [username, setUsername] = useState("");
  const [userId, setUserId] = useState("");

  
  const [selectedPost, setSelectedPost] = useState<LongText | null>(null);
  
  const handleLogout = () => {
    sessionStorage.removeItem("authHeader");
    setAuthHeader("");
    setIsLoggedIn(false);
  };

  const posts = [
  { id: 1, title: "첫 번째 글", score: 0 },
  { id: 2, title: "두 번째 글", score: 3 },
  { id: 3, title: "세 번째 글", score: 7 },
  { id: 4, title: "네 번째 글", score: 12 },
  { id: 5, title: "다섯 번째 글", score: 20 },
  ];
  
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

  const [textList, setTextList] = useState<LongText[]>([]);


// 긴글 목록 불러오기
useEffect(() => {
  if (!isLoggedIn || !authHeader) return;

  const fetchLyrics = async () => {
    try {
      const [allRes, myRes, scoreRes] = await Promise.all([
        axios.get("http://localhost:8080/long-text"),
        axios.get("http://localhost:8080/my-long-text", {
          headers: { Authorization: authHeader },
          withCredentials: true,
        }),
        axios.get("http://localhost:8080/long-text/scores", {
          withCredentials: true,
        }),
      ]);

      const allText: LongText[] = allRes.data.data.map((item: any) => ({
        longTextId: item.longTextId,
        title: item.title,
        isUserFile: false,
      }));

      const myText: LongText[] = myRes.data.map((item: any) => ({
        longTextId: item.myLongTextId,
        title: item.title,
        isUserFile: true,
      }));

      const combined = [...allText, ...myText];

      const scoreMap: Record<number, number> = {};

      // TODO : myfile일때 어케할지
      scoreRes.data.data.forEach((s: any) => {
        scoreMap[s.longScoreId] = s.score;
      });
      // 글 + 점수 합치기
      const merged = combined.map(post => ({
        ...post,
        score: scoreMap[post.longTextId] ?? null,
      }));

      setTextList(merged);
    } catch (err) {
      console.error("점수 + 글 불러오기 실패", err);
    }
  };

  fetchLyrics();
}, [isLoggedIn, authHeader]);

  return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
        <ProfileCard>
          <SearchContainer>
            <LogoContainer >
              <img src="/defaultprofile.png" alt="Logo" />
              <input 
                value={username}
                onChange={e => setUsername(e.target.value)}
              />
            </LogoContainer>
            <nav>
              <ol>
                <li><button onClick={handleLogout}>로그아웃</button></li>
                <li><button onClick={handleUpdateProfile}>회원정보수정</button></li>
                <li><button onClick={handleDeleteProfile}>탈퇴하기</button></li>
              </ol>
            </nav>
          </SearchContainer>

          <ScoreGrid>
            {textList.map(post => (
              <div 
                key={`${post.isUserFile ? "my" : "all"}-${post.longTextId}`}
                title={`${post.title} (score: ${post.score ?? "없음"})`}
                className="cell"
                data-score={post.score}
                onClick={() => setSelectedPost(post)}
              />
            ))}
          </ScoreGrid>
          {selectedPost && (
          <div className="info-box">
            <h3>{selectedPost.title}</h3>
            <p>ID: {selectedPost.longTextId}</p>
            {selectedPost.isUserFile && <span>내 글🐻‍❄️</span>}
            <p>점수 {selectedPost.score || "기록 전"}</p>
          </div>
        )}
        </ProfileCard>
      </Content>
    </Box>
  );
};

export default Profile;

const ProfileCard = styled.aside`
  width : 250px;
  margin-top : 40px;
`

const SearchContainer = styled.div`
  button {
    font-size : 14px;
    margin : 0.3rem 0rem;
    cursor : pointer;
  }
  ol{
    list-style:none;
  }
  button:hover{
    box-shadow: inset 0 -10px 0  #fcf3d9; 
  }
`

const LogoContainer = styled.div`
  display:flex;
  gap : 10px;
  margin-bottom : 10px;
  line-height: 2;
  img{
    height: 40px;
    width: auto;
  }
`


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


const ScoreGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(10, 16px);
  gap: 4px;
  margin-top: 20px;
  cursor:pointer;

  .cell {
    width: 16px;
    height: 16px;
    border-radius: 3px;
    background: #ebedf0;
    transition: background 0.3s;
  }
  .cell[data-score="0"] {
    background: #ebedf0;
  }
  .cell[data-score]:not([data-score="0"]) {
    background: #c6e48b;
  }
`;
