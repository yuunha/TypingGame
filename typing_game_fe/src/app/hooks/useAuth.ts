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

    const checkLogin = async () => {
      try {
        const res = await axios.get("http://localhost:8080/user", {
          headers: { Authorization: authHeader },
          withCredentials: true,
        });
        if (res.status === 200) {
          setIsLoggedIn(true);
          setUsername(res.data.username);
          setUserId(res.data.userId);
        }
      } catch {
        sessionStorage.removeItem("authHeader");
        setIsLoggedIn(false);
      }
    };

    checkLogin();
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
