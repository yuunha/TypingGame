'use client';

import React from "react";
import '../app/globals.css';
import styled from "styled-components";

import { useQuote } from "@/app/hooks/useQuote";
import Keyboard from './_components/Keyboard';
import keys from './_components/keyboard/keys'
import TypingLocal from "./_components/TypingLocal";

export default function Home() {
  
  const quote = useQuote();
return (
  <Content>
    <Header>
      <Title>오늘의 글</Title>
      <Title>{quote.author}</Title>
    </Header>
    <MainWrapper>
      <TypingLocal lyrics={quote.content ?? ""}/>
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
  padding : 0 2px ;
`;


const Title = styled.h1`
  font-family: NunumHumanBold, Helvetica, sans-serif;
  margin-bottom: 0.5rem;
`;

const MainWrapper = styled.div`
  width: var(--tpg-basic-with);
`;
