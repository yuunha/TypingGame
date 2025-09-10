"use client";
import { useState, useEffect } from "react";

export const useQuote = () => {
  const [quote, setQuote] = useState<{ author: string | null, content: string | null }>({
    author: null, content: null,});


  useEffect(() => {
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;

    fetch(`${baseUrl}/quote/today`)
    .then(res => {
        if (!res.ok) throw new Error(`Quote 불러오기 에러: ${res.status}`);
        return res.json();
    })
    .then(data => {
        setQuote({ author: data.author, content: data.content });
    })
    .catch(error => {
    console.error("Quote 불러오기 실패:", error);
    });
  }, []);
  return quote;
};
