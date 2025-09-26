"use client";

import React, { useState } from "react";
import { useAuth } from "@/hooks/useAuth";

export default function LoginForm() {
  const { promptLogin } = useAuth();
  const [loginId, setLoginId] = useState("");
  const [password, setPassword] = useState("");

  return (
    <form onSubmit={promptLogin}>
      <div className="border border-gray-300 rounded-[15px] p-2.5 text-sm">
        <input
          className="w-full p-1.5 border-b border-gray-200 focus:outline-none placeholder-gray-400 placeholder:text-sm"
          placeholder="아이디"
          value={loginId}
          onChange={(e) => setLoginId(e.target.value)}
        />
        <input
          className="w-full p-1.5 focus:outline-none placeholder-gray-400 placeholder:text-sm"
          placeholder="비밀번호"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
        />
      </div>
      <button
        type="submit"
        className="mt-5 w-full py-1.5 rounded-lg border border-black hover:bg-[var(--key-fill-red)] transition-colors"
      >
        Login
      </button>
    </form>
  );
}
