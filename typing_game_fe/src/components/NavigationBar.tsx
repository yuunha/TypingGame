"use client";

import React, { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

const NavigationBar: React.FC = () => {
  const [username, setUsername] = useState<string | null>(null);
  const router = useRouter();
 
    // 현재 로그인 사용자 정보 로드
    useEffect(() => {
        const fetchUser = async () => {
        try {
            const res = await fetch("http://localhost:8080/auth/me", {
            method: "GET",
            credentials: "include", // 세션 쿠키 유지
            });

            if (res.ok) {
            const data = await res.json();
            setUsername(data.username);
            } else {
            setUsername(null);
            }
        } catch (error) {
            console.error("사용자 정보 로딩 실패:", error);
            setUsername(null);
        }
        };

        fetchUser();
    }, []);


  return (
    <nav className="w-full bg-gray-200 text-black px-6 py-3 flex justify-between items-center">
      {/* 로고 */}
      <div
        className="text-xl font-bold cursor-pointer"
        onClick={() => router.push("/")}
      >
        타자연습
      </div>

      {/* 우측 버튼들 */}
      <div className="flex items-center gap-4">
        {username ? (
          <>
            <span className="font-semibold">{username} 님</span>
            <button
              onClick={() => router.push("/mypage")}
              className="bg-blue-500 px-3 py-1 rounded hover:bg-blue-600"
            >
              마이페이지
            </button>
            <button
              className="bg-red-500 px-3 py-1 rounded hover:bg-red-600"
            >
              로그아웃
            </button>
          </>
        ) : (
          <button
            onClick={() => router.push("/login")}
            className="bg-green-500 px-3 py-1 rounded hover:bg-green-600"
          >
            로그인
          </button>
        )}
      </div>
    </nav>
  );
};

export default NavigationBar;