"use client";
import React, { useEffect, useState } from "react";

import Image from "next/image";
import styled from "styled-components";
import axios from "axios";
import { useRouter } from "next/navigation";
import KeyboardMini from "@/app/_components/KeyboardMini";
import authKeys from "../../_components/keyboard/authKeys";
import { useAuth } from "@/app/hooks/useAuth"
import { useUserActions } from "@/app/hooks/useUserActions"

interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
  score?: number;
}

interface AllTextItem {
  longTextId: number;
  title: string;
}

interface MyTextItem {
  myLongTextId: number;
  title: string;
}

interface ScoreItem {
  longScoreId: number;
  score: number;
}

const Profile: React.FC = () => {
  const router = useRouter();
  const { username, userId, isLoggedIn, setUsername } = useAuth();
  const { updateProfile, deleteProfile } = useUserActions();

  const [localUsername, setLocalUsername] = useState(username || "");
  const [textList, setTextList] = useState<LongText[]>([]);
  const [selectedPost, setSelectedPost] = useState<LongText | null>(null);


  useEffect(() => {
    if (username) setLocalUsername(username);
  }, [username]);

  const handleLogout = () => {
    sessionStorage.removeItem("authHeader");
    router.push("/");
  };

  const handleUpdateProfile = async () => {
    if (!userId) return;
    try {
      await updateProfile(userId, localUsername);
      setUsername(localUsername);
      alert("회원정보가 수정되었습니다.");
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleDeleteProfile = async () => {
    if (!userId) return;
    try {
      await deleteProfile();
      sessionStorage.removeItem("authHeader");
      router.push("/");
      alert("회원 탈퇴 성공");
    } catch (err) {
      console.error(err);
      alert("탈퇴 실패");
    }
  };



// 긴글 목록 불러오기
useEffect(() => {
    const authHeader = sessionStorage.getItem("authHeader");
    if (!isLoggedIn || !authHeader) return;

  const fetchLyrics = async () => {
    try {
      const baseUrl = process.env.NEXT_PUBLIC_API_URL;
      
      const [allRes, myRes, scoreRes] = await Promise.all([
        axios.get(`${baseUrl}/long-text`),
        axios.get(`${baseUrl}/my-long-text`, {
          headers: { Authorization: authHeader },
          withCredentials: true,
        }),
        axios.get(`${baseUrl}/long-text/scores`, {
          withCredentials: true,
        }),
      ]);

      // any 타입 XX
      const allText: LongText[] = allRes.data.data.map((item: AllTextItem) => ({
        longTextId: item.longTextId,
        title: item.title,
        isUserFile: false,
      }));

      const myText: LongText[] = myRes.data.map((item: MyTextItem) => ({
        longTextId: item.myLongTextId,
        title: item.title,
        isUserFile: true,
      }));

      const combined = [...allText, ...myText];

      const scoreMap: Record<number, number> = {};

      // TODO : myfile일때 어케할지
      scoreRes.data.data.forEach((s: ScoreItem) => {
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
}, [isLoggedIn]);

  return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
        <ProfileCard>
          <SearchContainer>
            <LogoContainer >
              <Image src="/defaultprofile.png" alt="Logo" width={40} height={40}/>
              <input value={localUsername} onChange={e => setUsername(e.target.value)} />
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
