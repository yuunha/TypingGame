"use client";
import React, { useState, useEffect } from "react";
import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터

import styled from "styled-components";

const RankPage: React.FC = () => {
  const [selectedSong, setSelectedSong] = useState(lyricsList[0]);
  const username = "admin";
    const password = "12345";
    const basicAuth = btoa(`${username}:${password}`);
    useEffect(() => {
    const fetchScore = async () => {
        try {
        const res = await fetch(`http://localhost:8080/long-text/score?title=${encodeURIComponent(selectedSong.title)}`, {
            method: "GET",
            headers: {
                "Authorization": `Basic ${basicAuth}`,
                "Content-Type": "application/json"
            }
        });

        console.log("응답 상태:", res.status, "응답 OK?", res.ok);
        console.log("선택된 글:", selectedSong.title)
        const text = await res.text();
        console.log(text);

        if (res.status === 401) {
            console.log("인증문제");
            // 처리 로직
        } else if (res.status === 403) {
            console.log("인가문제.");
        }
        } catch (error) {
            console.log("유효성문제");
        }
    };

    fetchScore();
    }, [selectedSong]);


  return (
    <>
      <Box>
        <Aside>
            <ContentWrapper>
                <div className="list">
                {lyricsList.map((song, index) => (
                    <a
                        key={`default-${index}`}
                        data-active={selectedSong.title === song.title}
                        onClick={() => setSelectedSong(song)}
                    >
                        {song.title}
                    </a>
                    ))}
                </div>
            
            </ContentWrapper>
        </Aside>
        <MainWrapper>
            <Title>랭킹</Title>
        </MainWrapper>
      </Box>
    </>
  );
}
export default RankPage;


const Box = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  position: relative;
  z-index: 1;
`;






const MainWrapper = styled.div`
  padding-left:2rem; padding-right : 6rem; 
  width: 950px;
  color: black;
  height : 43rem;
  background-color: rgba(255, 255, 255);
  border-radius : 0 20px 20px 0;
`;



const Title = styled.h1`
  font-size: 2.5rem; /* text-2xl */
  font-weight: bold;
  margin-bottom: 1.5rem; /* mb-6 */
  margin-top : 70px;
`;


const Aside = styled.aside`
  width: 16rem;
  height: 43rem;
  min-width: 16rem;
  padding: 1.7rem;
  background-color: rgba(255, 255, 255);
  backdrop-filter: blur(2px);
  border-radius: 20px 0 0 20px;
  color: white;

  display: flex;
  flex-direction: column;
  justify-content: space-between; // 추가됨
`;

const ContentWrapper = styled.div`

  .list {
    display: flex;
    overflow: scroll;

    scrollbar-width: none;
    -ms-overflow-style: none;
    ::-webkit-scrollbar {
      width: 0;
      height: 0;
    }

    @media (min-width: 1024px) {
      display: block;
    }

    a {
      display: block;
      padding: 0.45rem;
      padding-left: 1rem;
      padding-right: 1rem;
      margin-top: 0.25rem;
      margin-bottom: 0.25rem;
      border-radius: 1rem;
      font-size: 0.875rem;
      line-height: 1.25rem;
      color: grey;
      flex-shrink: 0;
      cursor: pointer;

      &:hover {
        background-color: #ededed;
      }
      &[data-active="true"] {
        color: grey;
        background-color: #ededed;

        :hover {
          background-color: #ededed;
        }
      }
    }
  }
`
