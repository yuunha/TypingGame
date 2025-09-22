"use client";
import { useState, useEffect } from "react";
import { Constitution } from '@/app/types/constitution'
import { ConsProgress } from '@/app/types/cons-progress'

export const useConstitution = () => {
  const [authHeader, setAuthHeader] = useState<string | null>(null);

  useEffect(() => {
    setAuthHeader(sessionStorage.getItem("authHeader"));
  }, []);
  const baseUrl = process.env.NEXT_PUBLIC_API_URL;
  const [constitution, setConstitution] = useState<Constitution | null>(null);
  const [consProgress, setConsProgress] = useState<ConsProgress | null>(null);

  const fetchConsNumber = async () => {
  if (!authHeader) return;
  try {
      const res = await fetch(`${baseUrl}/constitution/progress`, {
      headers: { Authorization: authHeader },
      credentials: "include",
      });
      const data = await res.json();

      if (res.ok) {
        setConsProgress(data);
      } else {
        console.warn("초기화", data);
        setConsProgress({ articleIndex: 0, lastPosition: 0 });
      }
    } catch (err) {
        console.error("유저 헌법 진행상황 조회 실패", err);
        setConsProgress({ articleIndex: 0, lastPosition: 0 });
    }
  };
  
  const fetchConstitution = async (articleIndex : number) => {
      if (!authHeader) return;
      try {
        const res = await fetch(`${baseUrl}/constitution/${articleIndex}`, {
            headers: { Authorization: authHeader },
            credentials: "include",
        });
        const data = await res.json();
        setConstitution(data);
      } catch (err) {
        console.error("헌법 조회 실패", err);
      }
  };

  const saveProgress = async (articleIndex: number, lastPosition: number) => {
    if (!authHeader) return;
    try {
      const res = await fetch(`${baseUrl}/constitution/progress`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: authHeader,
        },
        credentials: "include",
        body: JSON.stringify({ articleIndex, lastPosition }),
      });

      if (!res.ok) {
        const errorData = await res.json();
        console.error("진행상황 저장 실패:", errorData);
      } else {
        console.log("진행상황 저장 성공");
      }
    } catch (err) {
      console.error("진행상황 저장 API 에러:", err);
    }
  };

  return { consProgress, fetchConsNumber, constitution, fetchConstitution, saveProgress };

};
