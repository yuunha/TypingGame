
import { useState, useEffect } from "react";
import { useAuth } from "@/app/hooks/useAuth";

interface Friend {
  id: number;
  username: string;
  avatarUrl?: string;
}

export const useFriend = () => {
  const authHeader = sessionStorage.getItem("authHeader");
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const [friends, setFriends] = useState<Friend[]>([]);
  const [sentRequests, setSentRequests] = useState<any[]>([]);
  const [receivedRequests, setReceivedRequests] = useState<any[]>([]);

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
        const res = await fetch(`${baseUrl}/users/search?nickname=${encodeURIComponent(query)}`, {
        headers: { Authorization: authHeader },
        credentials: "include",
        });
        const data = await res.json();
        setSearchResults(data);
    } catch (err) {
        console.error("유저 검색 실패", err);
    }
    //TODO: 친구 신청 기능
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
  };
};