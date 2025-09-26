"use client";
import React, { useState } from "react";
import Image from "next/image";
import { useAuth } from "@/hooks/useAuth";
import ProfileEditMenu from "../../../../features/auth/profile/components/ProfileEditMenu";

export default function ProfileEdit() {
  const { username } = useAuth();
  const [preview, setPreview] = useState<string | null>(null);

  return (
    <div className="flex flex-col">
      <aside className="w-[var(--tpg-basic-width)] mt-10">
        <div className="flex items-center gap-2.5 mb-2.5">
          <Image
            src={preview ?? "/defaultprofile.png"}
            alt="Logo"
            width={80}
            height={80}
            className="w-20 h-20 rounded-full object-cover"
          />
          <span>{username}</span>
        </div>
        <ProfileEditMenu setPreview={setPreview} />
      </aside>
    </div>
  );
};
