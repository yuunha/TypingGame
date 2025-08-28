"use client";
import { useState, useEffect } from "react";
import axios from "axios";

export const useAuth = () => {
  const [username, setUsername] = useState<string | null>(null);
  const [userId, setUserId] = useState<string | null>(null);
  const [isLoggedIn, setIsLoggedIn] = useState<boolean>(false);

  useEffect(() => {
    const authHeader = sessionStorage.getItem("authHeader");
    if (!authHeader) {
      setIsLoggedIn(false);
      return;
    }
    const baseUrl = process.env.NEXT_PUBLIC_API_URL
    
    fetch(`${baseUrl}/user`,{
      headers: { Authorization: authHeader },
      credentials: "include",
    })
    .then((response)=>{
      if(!response.ok) throw new Error(`에러 발생 : ${response.status}`);
      return response.json();
    })
    .then((data)=>{
      setIsLoggedIn(true);
      setUsername(data.username);
      setUserId(data.userId)
    })
    .catch((error)=> console.error(error))
  }, []);

  const promptLogin = async () => {
    if (isLoggedIn) return;

    const usernameInput = prompt("아이디를 입력하세요") || "";
    const passwordInput = prompt("비밀번호를 입력하세요") || "";
    if (!usernameInput || !passwordInput) {
      alert("로그인 정보가 필요합니다.");
      return;
    }

    const basicAuth = "Basic " + btoa(`${usernameInput}:${passwordInput}`);
    sessionStorage.setItem("authHeader", basicAuth);
    setUsername(usernameInput);
    setIsLoggedIn(true);
  };

  return { username, userId, isLoggedIn, promptLogin, setUsername };
};
