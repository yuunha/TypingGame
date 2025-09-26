"use client";
import React, { useState } from "react";

import { LongText } from "@/types/long-text";
import { useLongTextWithScore } from "@/features/auth/profile/hooks/useLongTextWithScore";

export default function ScoreGrid(){
  const [selectedPost, setSelectedPost] = useState<LongText | null>(null);
  const longTexts = useLongTextWithScore();

  return (
    <>
        <div className="grid grid-cols-30 gap-1 mt-5 cursor-pointer">
            {longTexts.map(post => (
            <div 
                key={`${post.isUserFile ? "my" : "all"}-${post.longTextId}`}
                title={`${post.title} (score: ${post.score ?? "없음"})`}
                className={`w-4 h-4 rounded-sm transition-colors ${
                post.score && post.score !== 0 ? "bg-[#c6e48b]" : "bg-gray-200"
                }`}
                onClick={() => setSelectedPost(post)}
            />
            ))}
        </div>

        {selectedPost && (
            <div className="w-60 mt-3 p-2 border rounded-md bg-gray shadow-md info-box">
                <h3>{selectedPost.title}</h3>
                <p className="text-sm">점수 {selectedPost.score || "기록 전"}</p>
            </div>
        )}
    </>
  );
};
