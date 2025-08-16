"use client";
import React, { useEffect, useState } from "react";
import authKeys from "../../_components/keyboard/authKeys";

import styled from "styled-components";
import axios from "axios";
import KeyboardMini from "@/app/_components/KeyboardMini";
import { useRouter } from "next/navigation";



const AuthMenu: React.FC = () => {
  return (
    <Box>
      <Content>
        <KeyboardMini keys={authKeys} />
      </Content>
    </Box>
  );
};

export default AuthMenu;



const Box = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
`;


const Content = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
`;


const MainWrapper = styled.div`
  width: 600px;
  color: black;
  border-radius: 0 20px 20px 0;
`;
