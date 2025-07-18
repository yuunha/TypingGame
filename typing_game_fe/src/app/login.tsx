"use client";

import React, { useState } from "react";

const LoginPage: React.FC = () => {
    const [loginId, setLoginId] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    const handleLogin = async (e: React.FormEvent) => {

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