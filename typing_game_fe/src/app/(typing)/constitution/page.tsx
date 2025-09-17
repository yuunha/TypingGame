"use client";
import React, { useState, useEffect } from "react";
import Keyboard from "../../_components/Keyboard";
import typingKeys from "../../_components/keyboard/typingKeys";
import styled from "styled-components";
import { Constitution } from '@/app/types/constitution'
import { useConstitution } from "@/app/hooks/useConstitution";
import Typing from "./Typing"

const ConstitutionTyping: React.FC = () => {
  const {constitution, fetchConstitution} = useConstitution();
  const [selectedCons, setSelectedCons] = useState<Constitution | null>(null);

  useEffect(() => {
    fetchConstitution(0);
  }, []);
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

const SidebarWrapper = styled.div`
  position: absolute;
  left: 0;
  top: 0;
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

const EmptyMessage = styled.h2`
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  min-height: 300px;
`;

const SubMessage = styled.p`
  font-size: 0.9rem;
  color: #666;
`;

const ProgressBarContainer = styled.div`
  height: 2px;
  background-color: var(--progress-bg);
  margin-bottom: 1.5rem;
`;
