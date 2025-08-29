'use client';

import React from "react";
import '../app/globals.css';
import styled from "styled-components";

import Keyboard from './_components/Keyboard';
import keys from './_components/keyboard/keys'

export default function Home() {
return (
  <>
    <div>
    <LogoImg src="tile.png" alt="Tile image" />
    <Keyboard keys = {keys} />
    </div>
  </>
  );
};

const LogoImg = styled.img`
  width:3rem;
`;