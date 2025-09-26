"use client";
import React from "react";

import Image from "next/image";
import { useAuth } from "@/hooks/useAuth"
import ProfileMenu from "../../../features/auth/profile/components/ProfileMenu";
import ScoreGrid from "../../../features/auth/profile/components/ScoreGrid";

export default function Profile() {
  const { username } = useAuth();

  return (
    <div className="flex flex-col">
      <aside className="w-[var(--tpg-basic-width)] mt-10">
        <div className="flex items-center gap-2.5 mb-2.5 leading-8">
          <Image src="/defaultprofile.png" alt="Logo" width={40} height={40} />
          <span>{username}</span>
        </div>
        <ProfileMenu />
        <ScoreGrid />
      </aside>
    </div>
  );
};
