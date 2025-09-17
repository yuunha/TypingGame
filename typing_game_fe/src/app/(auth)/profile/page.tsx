"use client";
import React, { useEffect, useState } from "react";

import Image from "next/image";
import styled from "styled-components";
import { useRouter } from "next/navigation";
import { useAuth } from "@/app/hooks/useAuth"
import { useUserActions } from "@/app/hooks/useUserActions"
import { LongText } from "../../types/long-text";
import { FiUser, FiLogOut, FiTrash2, FiEdit } from "react-icons/fi";
import { useLongTextWithScore } from "@/app/hooks/useLongTextWithScore";
import Link from "next/link"; 

const Profile: React.FC = () => {
  const router = useRouter();
  const { username, userId, isLoggedIn, setUsername } = useAuth();
  const { updateProfile, deleteProfile } = useUserActions();

  const [localUsername, setLocalUsername] = useState(username || "");
  const [textList, setTextList] = useState<LongText[]>([]);
  const [selectedPost, setSelectedPost] = useState<LongText | null>(null);

  useEffect(() => {
    if (username) setLocalUsername(username);
    // if (!isLoggedIn) router.push("/login");
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


  const longTexts = useLongTextWithScore();

  return (
    <Content>
      <ProfileCard>
        <LogoContainer >
          <Image src="/defaultprofile.png" alt="Logo" width={40} height={40}/>
          <input value={localUsername} onChange={e => setUsername(e.target.value)} />
        </LogoContainer>
        <nav>
          <SubMenu>
            <li onClick={() => router.push("/friends")}>
              <FiUser /> 친구
            </li>
            <li onClick={handleLogout}>
              <FiLogOut /> 로그아웃
            </li>
            
            <li>
              <FiEdit /><Link href="/profile/edit">회원정보 수정</Link>
            </li>
            <li onClick={handleDeleteProfile}>
              <FiTrash2 /> 탈퇴
            </li>
          </SubMenu>
        </nav>

        <ScoreGrid>
          {longTexts.map(post => (
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
  );
};

export default Profile;



const Content = styled.div`
  display: flex;
  flex-direction: column;
`;


const ProfileCard = styled.aside`
  width: var(--tpg-basic-width);
  margin-top : 40px;
`


const LogoContainer = styled.div`
  display:flex;
  gap : 10px;
  margin-bottom : 10px;
  line-height: 2;
`

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


const SubMenu = styled.ul`
  list-style: none;
  padding: 0;
  margin-top: 10px;

  li {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 10px;
    cursor: pointer;
    border-radius: 8px;
    transition: background 0.2s;
    font-size: 14px;

    &:hover {
      background-color: var(--sub-menu-active);
    }

    svg {
      flex-shrink: 0;
    }
  }
`;