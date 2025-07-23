"use client";

import React, { useState } from "react";
import { useRouter } from "next/navigation";


const LoginPage: React.FC = () => {
    const [loginId, setLoginId] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const router = useRouter();

    const handleLogin = async (e: React.FormEvent) => {
      e.preventDefault(); // ✅ 기본 새로고침 방지
      console.log("로그인 요청 시작..."); 
      try{
        const res = await fetch("http://localhost:8080/auth/login", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: new URLSearchParams({
            loginId,
            password,
          }),
        });

        console.log("응답 상태:", res.status);
        console.log("응답 OK?", res.ok);
  
        const text = await res.text();
        setMessage(text);
        if (res.status === 200) {
          router.push("/"); // 메인 페이지로 이동
        }else if (res.status === 401) {
          setMessage("로그인 실패: 아이디 또는 비밀번호가 올바르지 않습니다.");
        }
      }catch(error){
        setMessage("로그인 요청 실패")
      }


    }
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
          <h1 className="text-2xl font-bold mb-6">로그인</h1>
          <form
            onSubmit={handleLogin}
            className="flex flex-col gap-4 bg-white p-6 rounded shadow-md w-80"
          >
            <input
              type="text"
              placeholder="아이디"
              value={loginId}
              onChange={(e) => setLoginId(e.target.value)}
              className="border rounded px-3 py-2"
            />
            <input
              type="password"
              placeholder="비밀번호"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="border rounded px-3 py-2"
            />
            <button
              type="submit"
              className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
            >
              로그인
            </button>
          </form>
          {message && <p className="mt-4 text-lg">{message}</p>}
        </div>
      );
    };
    
export default LoginPage;