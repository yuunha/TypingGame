"use client";
import React, { useState } from "react";
import { useAuth } from "@/hooks/useAuth"
import { useUserActions } from "@/features/auth/profile/hooks/useUserActions"
import { FiImage, FiEdit } from "react-icons/fi";
import { ProfileMenuItem } from "./ProfileMenuItem";
import { useRouter } from "next/navigation";

interface ProfileEditMenuProps {
  setPreview: (url: string | null) => void;
}
export default function ProfileEditMenu({ setPreview }: ProfileEditMenuProps)
{
  const { username, userId, setUsername } = useAuth();
  const { updateProfile, updateProfileImg } = useUserActions();
  const [localUsername, setLocalUsername] = useState(username ?? "");
  const [file, setFile] = useState<File | null>(null);

  const router = useRouter();
  const handleUpdateProfile = async () => {
        if (!userId) return;
        try {
            await updateProfile(userId, localUsername);
                setUsername(localUsername);
                alert("회원정보가 수정되었습니다.");
                router.push("/profile");
            } catch (err) {
                alert("수정 실패");
            }
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = e.target.files?.[0];
        if (!selectedFile) return;
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile));
    };

    const handleUploadImage = () => {
        if (!file) return;
        updateProfileImg(file);
    };

  return (
    <nav>
      <ul className="flex flex-col mt-2 gap-2 text-sm">
        <input
        type="text"
        className="border border-gray-300 rounded px-3 py-1.5 max-w-[200px] focus:outline-none focus:ring-1 focus:ring-gray-400"
        value={localUsername}
        onChange={(e) => setLocalUsername(e.target.value)}
        placeholder="수정할 이름"
        />
        <ProfileMenuItem icon={<FiEdit />} label="이름 수정" onClick={handleUpdateProfile} />
        
        <input
        type="file"
        accept="image/*"
        onChange={handleFileChange}
        className="border border-gray-300 rounded px-3 py-1.5 max-w-[200px] file:border-0 file:bg-gray-100 file:rounded file:px-2 file:py-1 file:text-sm file:cursor-pointer focus:outline-none focus:ring-1 focus:ring-gray-400"
        />
        <ProfileMenuItem icon={<FiImage />} label="프로필 사진 업로드" onClick={handleUploadImage} />
      </ul>
    </nav>
  );
};
