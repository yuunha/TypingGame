"use client";
import React, { useEffect, useState } from "react";

import styled from "styled-components";
import axios from "axios";
import { useRouter } from "next/navigation";
import KeyboardMini from "@/app/_components/KeyboardMini";
import authKeys from "../../_components/keyboard/authKeys";
import { useAuth } from "@/app/hooks/useAuth"
import Image from "next/image";



interface LongText {
  longTextId: number;
  title: string;
  isUserFile?: boolean;
  score?: number;
}

const Friend: React.FC = () => {
  const { isLoggedIn, promptLogin } = useAuth();
  const router = useRouter();

  useEffect(() => {
      if (!isLoggedIn) promptLogin();
    }, [isLoggedIn]);

  return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
        <ProfileCard>
        <h1>친구 목록</h1>
          <SearchContainer>
            <nav>
              <ol>
                <li>
                    <LogoContainer >
                        <Image src="/defaultprofile.png" alt="Logo" />
                    </LogoContainer>
                </li>
              </ol>
            </nav>
          </SearchContainer>

        </ProfileCard>
      </Content>
    </Box>
  );
};

export default Friend;

const ProfileCard = styled.aside`
  width : 250px;
  margin-top : 40px;
`

const SearchContainer = styled.div`
  button {
    font-size : 14px;
    margin : 0.3rem 0rem;
    cursor : pointer;
  }
  ol{
    list-style:none;
  }
  button:hover{
    box-shadow: inset 0 -10px 0  #fcf3d9; 
  }
`

const LogoContainer = styled.div`
  display:flex;
  gap : 10px;
  margin-bottom : 10px;
  line-height: 2;
  img{
    height: 30px;
    width: auto;
  }
`


const Box = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background: var(--background);
`;


const Content = styled.div`
  margin-top : 100px;
  flex-direction: column;
  align-items: center;
  display:flex;
`;


const ScoreGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(10, 16px);
  gap: 4px;
  margin-top: 20px;
  cursor:pointer;

  .cell {
    width: 16px;
    height: 16px;
    border-radius: 3px;
    background: #ebedf0;
    transition: background 0.3s;
  }
  .cell[data-score="0"] {
    background: #ebedf0;
  }
  .cell[data-score]:not([data-score="0"]) {
    background: #c6e48b;
  }
`;
