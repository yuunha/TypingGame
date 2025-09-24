"use client";
import React, { useEffect, useState } from "react";

import styled from "styled-components";
import { useFriend } from "@/app/hooks/useFriend";
import { useFriendActions } from "@/app/hooks/useFriendActions";
import { FiUsers, FiArrowUpCircle, FiArrowDownCircle, FiUserPlus } from "react-icons/fi";

const FriendPage: React.FC = () => {

  const [activeTab, setActiveTab] = useState<"list" | "add" | "sent" | "received">("list");
  const [searchQuery, setSearchQuery] = useState("");
  const { friends, searchResults, sentRequests, receivedRequests, fetchFriends, handleSearch, fetchSentRequests, fetchReceivedRequests, sentFriendRequest } = useFriend();
  const { deleteFriend, handleReceivedRequests } = useFriendActions();

  useEffect(() => {
    if (activeTab === "list") fetchFriends();
    else if (activeTab === "add") handleSearch(searchQuery);
    else if (activeTab === "sent") fetchSentRequests();
    else if (activeTab === "received") fetchReceivedRequests();

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
              {friends.length === 0 ? <p>친구가 없습니다.</p> : friends.map(f => (
              <FriendItem key={f.userId}>
                <UserProfile> 
                  <UserProfileImg src='/g72.jpg'/>
                  {f.username}
                </UserProfile>
                {/* <FrDeleteBtn onClick={()=> deleteFriend(f.requestId)}>삭제</FrDeleteBtn> */}
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
                {searchResults.length === 0 ? <></> : searchResults.map(f => (
                  <FriendItem key={f.userId}>
                    <UserProfile> 
                      <UserProfileImg src='/g72.jpg'/>
                      {f.username}
                    </UserProfile>
                    <FrAcceptBtn onClick={() => sentFriendRequest(f.userId)}>추가</FrAcceptBtn>
                  </FriendItem>
                ))}
              </ul>
            </>
          )}
          {activeTab === "sent" && (
            <>
              <h2>보낸 요청</h2>
              {sentRequests.length === 0 ? <p>보낸 요청이 없습니다.</p> : sentRequests.map(r => (
                <FriendItem key={r.friendRequestId}>
                  <UserProfile> 
                    <UserProfileImg src='/g72.jpg'/>
                      {r.receiverName}
                  </UserProfile>
                  <FrBtnWrapper>
                    <FrDeleteBtn>취소</FrDeleteBtn>
                  </FrBtnWrapper>
                </FriendItem>
              ))}
            </>
          )}
          {activeTab === "received" && (
            <>
              <h2>받은 요청</h2>
              {receivedRequests.length === 0 ? <p>받은 요청이 없습니다.</p> : receivedRequests.map(r => (
                <FriendItem key={r.friendRequestId}> 
                  <UserProfile> 
                    <UserProfileImg src='/g72.jpg'/>
                      {r.requesterName}
                  </UserProfile>
                  <FrBtnWrapper>
                    <FrAcceptBtn onClick={() => handleReceivedRequests(r.friendRequestId, "ACCEPT")}>허용</FrAcceptBtn>
                    <FrDeleteBtn onClick={() => handleReceivedRequests(r.friendRequestId, "DECLINE")}>거부</FrDeleteBtn>
                  </FrBtnWrapper>
                </FriendItem>
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
  justify-content: space-between;
  margin-top: 15px;
`;

const UserProfileImg = styled.img`
  width: 35px;
  height: 35px;
  border-radius: 50%;
  object-fit: cover;
`;

const FrBtnWrapper = styled.div`
  display: flex;
  gap : 10px;
`

const FrDeleteBtn = styled.span`
  cursor:pointer;
  background-color : var(--cus-friend-delete);
  font-size : 0.8rem;
  padding : 1px 3px;
  border-radius : 5px;
  color : white;
`;

const FrAcceptBtn = styled.span`
  cursor:pointer;
  background-color : var(--cus-friend-accept);
  font-size : 0.8rem;
  padding : 1px 3px;
  border-radius : 5px;
  color : white;
`;


const UserProfile = styled.div`
  display: flex;
  gap : 10px;
  align-items: center;
`