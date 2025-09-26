import React from "react";
import { useRouter } from "next/navigation";
import { useUserActions } from "@/features/auth/profile/hooks/useUserActions"
import { FiUser, FiLogOut, FiTrash2, FiEdit } from "react-icons/fi";
import { ProfileMenuItem } from "./ProfileMenuItem";


export default function ProfileMenu(){
  const router = useRouter();
  const { deleteProfile } = useUserActions();

  const handleLogout = () => {
    sessionStorage.removeItem("authHeader");
    router.push("/");
  };

  const handleDeleteProfile = async () => {
    try {
      await deleteProfile();
      sessionStorage.removeItem("authHeader");
      router.push("/");
      alert("회원 탈퇴 성공");
    } catch (err) {
      console.error(err);
      alert("탈퇴 실패");
    }
  };

  return (
    <nav>
        <ul className="flex flex-col mt-2 gap-1 text-sm">
            <ProfileMenuItem
            icon={<FiUser />}
            label="친구"
            onClick={() => router.push("/friends")}
            />
            <ProfileMenuItem
            icon={<FiLogOut />}
            label="로그아웃"
            onClick={handleLogout}
            />
            <ProfileMenuItem
            icon={<FiEdit />}
            label="회원정보 수정"
            href="/profile/edit"
            />
            <ProfileMenuItem
            icon={<FiTrash2 />}
            label="탈퇴"
            onClick={handleDeleteProfile}
            />
        </ul>
    </nav>
  );
};
