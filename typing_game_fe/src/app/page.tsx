'use client';

import React from "react";
import '../app/globals.css';
import styled from "styled-components";

import Keyboard from './_components/Keyboard';
import keys from './_components/keyboard/keys'
import TypingLocal from "./_components/TypingLocal";

export default function Home() {
  const lyricsList = {
      author: "나태주",
      title: "너를 사랑하는 나의 맘",
      content: "너의 꾸밈없음과 꿈 많음을 사랑한다",
  };

return (
  <Content>
    <Header>
      <Title>오늘의 글</Title>
      <Title>{lyricsList.author}〈{lyricsList.title}〉</Title>
    </Header>
    <MainWrapper>
      <TypingLocal lyrics={lyricsList.content}/>
    </MainWrapper>
    <Keyboard keys={keys} />
  </Content>
  );
};

const Content = styled.div`
  display: flex;
  flex-direction: column;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  width: 100%;
  font-size: var(--tpg-header-font-size);
  // margin-top: 3rem;
  padding : 0 2px ;
`;


const Title = styled.h1`
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const MainWrapper = styled.div`
  width: var(--tpg-basic-with);
`;
