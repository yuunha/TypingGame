"use client";
import React, { useEffect, useState } from "react";

import styled from "styled-components";
import { useFriend } from "@/app/hooks/useFriend";
import { FiUsers, FiArrowUpCircle, FiArrowDownCircle, FiUserPlus } from "react-icons/fi";
import { d } from "hangul-js";

interface Friend {
  id: number;
  username: string;
  avatarUrl?: string;
}

const FriendPage: React.FC = () => {

  const [activeTab, setActiveTab] = useState<"list" | "add" | "sent" | "received">("list");
  const { friends, searchResults, sentRequests, receivedRequests, fetchFriends, handleSearch, fetchSentRequests, fetchReceivedRequests } = useFriend();
  const [searchQuery, setSearchQuery] = useState("");
  const [dfriends, dsetFriends] = useState<Friend[]>([]);

  useEffect(() => {
    if (activeTab === "list") fetchFriends();
    else if (activeTab === "add") handleSearch(searchQuery);
    else if (activeTab === "sent") fetchSentRequests();
    else if (activeTab === "received") fetchReceivedRequests();

    dsetFriends([
      { id: 1, username: "유저1" },
      { id: 2, username: "유저2" },
      { id: 3, username: "유저3" },
    ]);
  }, [activeTab]);


  return (
      <Content>
        <nav>
          <SubMenu>
            <li onClick={() => setActiveTab("list")} className={activeTab === "list" ? "active" : ""}>
              <FiUsers /> 친구 목록
            </li>
            <li onClick={() => setActiveTab("add")} className={activeTab === "add" ? "active" : ""}>
              <FiUserPlus /> 친구 추가
            </li>
            <li onClick={() => setActiveTab("sent")} className={activeTab === "sent" ? "active" : ""}>
              <FiArrowUpCircle /> 보낸 요청
            </li>
            <li onClick={() => setActiveTab("received")} className={activeTab === "received" ? "active" : ""}>
              <FiArrowDownCircle /> 받은 요청
            </li>
          </SubMenu>

        </nav>
        <FriendWrapper>
          {activeTab === "list" && (
            <>
              <h2>친구 목록</h2>
              {dfriends.length === 0 ? <p>친구가 없습니다.</p> : dfriends.map(f => (
              <FriendItem key={f.id}>
                <UserProfileImg src='/b3.jpg'/>
                <UserName>{f.username}</UserName>
                <DeleteFriendBtn>x</DeleteFriendBtn>
              </FriendItem>
            ))}
            </>
          )}
          {activeTab === "add" && (
            <>
              <h2>검색</h2>
              <input
                type="text"
                placeholder="닉네임 입력"
                value={searchQuery}
                onChange={e => setSearchQuery(e.target.value)}
              />
              <button onClick={() => handleSearch(searchQuery)}>검색</button>
              <ul>
                {searchResults.map(user => (
                  <li key={user.id}>{user.username}</li>
                ))}
              </ul>
            </>
          )}
          {activeTab === "sent" && (
            <>
              <h2>보낸 요청</h2>
              {sentRequests.length === 0 ? <p>보낸 요청이 없습니다.</p> : sentRequests.map(r => (
                <FriendItem key={r.id}>{r.username}</FriendItem>
              ))}
            </>
          )}
          {activeTab === "received" && (
            <>
              <h2>받은 요청</h2>
              {receivedRequests.length === 0 ? <p>받은 요청이 없습니다.</p> : receivedRequests.map(r => (
                <FriendItem key={r.id}>{r.username}</FriendItem>
              ))}
            </>
          )}

        </FriendWrapper>
      </Content>
  );
};

export default FriendPage;


const Content = styled.div`
  display: flex;
  width: var(--tpg-basic-width);
  gap : 40px;
  margin-top: 10px;
`;

const FriendWrapper = styled.div`
  flex: 1;
`

const SubMenu = styled.ul`
  list-style: none;
  padding: 0;
  width : 200px;

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

    &.active {
      background-color: var(--sub-menu-active);
    }

    svg {
      flex-shrink: 0;
    }
  }
`;

const FriendItem = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  border-bottom: 1px solid #ddd;
`;

const UserProfileImg = styled.img`
  width: 24px;
  height: 24px;
  border-radius: 50%;
  object-fit: cover;
`;
const UserName = styled.div`
`;
const DeleteFriendBtn = styled.span`
  cursor:pointer;
`;