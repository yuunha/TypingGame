"use client";
import { useState, useEffect } from "react";

interface Friend {
  userId: number;
  username: string;
  profileImg?: string;
}

interface FriendRequest {
  friendRequestId: number;
  createdAt: string;
  receiverName: number;
  requesterName: number;
}

interface UserResponse {
  userId: number;
  username: string;
  profileImageUrl?: string | null;
}

export const useFriend = () => {
  const [authHeader, setAuthHeader] = useState<string | null>(null);

  useEffect(() => {
    setAuthHeader(sessionStorage.getItem("authHeader"));
  }, []);
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const [friends, setFriends] = useState<Friend[]>([]);
  const [sentRequests, setSentRequests] = useState<FriendRequest[]>([]);
  const [receivedRequests, setReceivedRequests] = useState<FriendRequest[]>([]);

  const [searchResults, setSearchResults] = useState<Friend[]>([]);

  //친구 목록 조회
  const fetchFriends = async () => {
    if (!authHeader) return;
    try {
      const res = await fetch(`${baseUrl}/friends`, {
        headers: { Authorization: authHeader },
        credentials: "include",
      });
      const data = await res.json();
      setFriends(data);
    } catch (err) {
      console.error("친구 목록 조회 실패", err);
    }
  };

  //유저 검색
  const handleSearch = async (query: string) => {
    if (!authHeader) return;
    try {
        const res = await fetch(`${baseUrl}/users?username=${query}&page=0&size=5`, {
        headers: { Authorization: authHeader },
        credentials: "include",
        });
        const data = await res.json();
        console.log(data)
        const friends: Friend[] = data.content.map((user: UserResponse)=>({
          id: user.userId,
          username: user.username,
          profileImg: user.profileImageUrl ?? undefined,
        }))
        setSearchResults(friends);
    } catch (err) {
        console.error("유저 검색 실패", err);
    }
  };
  //친구요청
  const sentFriendRequest = async (receiverId: number) => {
    if (!authHeader) return;
    try {
        await fetch(`${baseUrl}/friend-requests`, {
          method: "POST",
          headers: { Authorization: authHeader, "Content-Type": "application/json",},
          credentials: "include",
          body: JSON.stringify({ receiverId }),
        });
    } catch (err) {
        console.error("친구 요청 실패", err);
    }
  };

  const fetchSentRequests = async () => {
    if (!authHeader) return;
    try {
      const res = await fetch(`${baseUrl}/friend-requests/sent`, {
        headers: { Authorization: authHeader },
        credentials: "include",
      });
      const data = await res.json();
      setSentRequests(data);
    } catch (err) {
      console.error("보낸 요청 조회 실패", err);
    }
  };

  const fetchReceivedRequests = async () => {
    if (!authHeader) return;
    try {
      const res = await fetch(`${baseUrl}/friend-requests/received`, {
        headers: { Authorization: authHeader },
        credentials: "include",
      });
      const data = await res.json();
      setReceivedRequests(data);
    } catch (err) {
      console.error("받은 요청 조회 실패", err);
    }
  };

  return {
    friends,
    searchResults,
    sentRequests,
    receivedRequests,
    fetchFriends,
    handleSearch,
    fetchSentRequests,
    fetchReceivedRequests,
    sentFriendRequest,
  };
};