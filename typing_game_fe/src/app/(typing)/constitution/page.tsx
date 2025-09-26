"use client";
import React, { useState, useEffect } from "react";
import Keyboard from "../../_components/keyboard/Keyboard";
import typingKeys from "../../_components/keyboard/key/typingKeys";
import styled from "styled-components";
import { Constitution } from '@/app/types/constitution'
import { useConstitution } from "@/app/hooks/useConstitution";
import Typing from "./Typing"

const ConstitutionTyping: React.FC = () => {

  const { consProgress, constitution, fetchConstitution, saveProgress } = useConstitution();
  const [selectedCons, setSelectedCons] = useState<Constitution | null>(null);

  useEffect(() => {
    if (consProgress === null) return;
    useConstitution();
  }, []);

  useEffect(() => {
    if (typeof consProgress?.articleIndex === "number") {
      fetchConstitution(consProgress.articleIndex);
    }
  }, [consProgress]);


  useEffect(() => {
    setSelectedCons(constitution);
  }, [constitution]);

  return (
    <Box>
      <Content>
            <Header>
              <Title>헌법 따라치기</Title>
              <Title>〈{selectedCons?.chapter}〉- {selectedCons?.articleNumber}</Title>
            </Header>
            <MainWrapper>
              <Typing
                content={selectedCons?.content ?? ""}
                articleIndex={selectedCons?.articleIndex ?? 0}
                lastPosition={consProgress?.lastPosition ?? 0}
                saveProgress={saveProgress}
              />
            </MainWrapper>
        <Keyboard keys={typingKeys}/>
      </Content>
    </Box>
  );
};

export default ConstitutionTyping;

const Box = styled.div`
  position: relative;
  align-items: center;
  height: 100vh;
  overflow: hidden;
`;


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
  font-weight: bold;
  margin-bottom: 0.5rem;
`;

const MainWrapper = styled.div`
  width: var(--tpg-basic-width);
`;
