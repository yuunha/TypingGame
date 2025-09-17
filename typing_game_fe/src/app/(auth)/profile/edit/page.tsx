"use client";
import React, { useState } from "react";
import Image from "next/image";
import styled from "styled-components";
import { useAuth } from "@/app/hooks/useAuth"
import { useUserActions } from "@/app/hooks/useUserActions"
import { FiImage, FiEdit } from "react-icons/fi";

const ProfileEdit: React.FC = () => {
  const { username, userId, setUsername } = useAuth();
  const { updateProfile, updateProfileImg } = useUserActions();
  const [localUsername, setLocalUsername] = useState(username ?? "");
  const [file, setFile] = useState<File | null>(null);
  const [preview, setPreview] = useState<string | null>(null);

  const handleUpdateProfile = async () => {
    if (!userId) return;
    try {
        await updateProfile(userId, localUsername);
            setUsername(localUsername);
            alert("회원정보가 수정되었습니다.");
        } catch (err) {
            alert("수정 실패");
        }
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selectedFile = e.target.files?.[0];
        if (!selectedFile) return;
        setFile(selectedFile);
        setPreview(URL.createObjectURL(selectedFile)); // 미리보기
    };


  return (
    <Content>
      <ProfileCard>
        <LogoContainer >
          <Image
            src={preview ?? "/defaultprofile.png"}
            alt="Logo"
            width={80}
            height={80}
          />
          {username}
        </LogoContainer>
        <nav>
          <SubMenu>
            <input 
            value={localUsername} 
            onChange={e => setLocalUsername(e.target.value)} 
            placeholder="수정할 이름"/>
            <li onClick={handleUpdateProfile}>
              <FiEdit /> 이름 수정
            </li>
            <input type="file" accept="image/*" onChange={handleFileChange} />
            <li onClick={() => updateProfileImg(file)}>
              <FiImage /> 프로필 사진 업로드
            </li>
          </SubMenu>
        </nav>
      </ProfileCard>
    </Content>
  );
};

export default ProfileEdit;



const Content = styled.div`
  display: flex;
  flex-direction: column;
`;


const ProfileCard = styled.aside`
  width: var(--tpg-basic-width);
  margin-top : 40px;
`


const LogoContainer = styled.div`
  display:flex;
  gap : 10px;
  margin-bottom : 10px;
  line-height: 2;
`



const SubMenu = styled.ul`
  list-style: none;
  padding: 0;
  margin-top: 10px;

  input{
    border : 1px solid black;
    margin-left : 0.5rem;
  }
  li {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 10px;
    cursor: pointer;
    border-radius: 8px;
    transition: background 0.2s;
    font-size: 14px;

    &:hover {
      background-color: var(--sub-menu-active);
    }

    svg {
      flex-shrink: 0;
    }
  }
`;