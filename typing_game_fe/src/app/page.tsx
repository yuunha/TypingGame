'use client';

import React, { useEffect, useState } from "react";
import styled from "styled-components";
import '../app/globals.css';

import Keyboard from './_components/Keyboard';
import keys from './_components/keyboard/keys'

export default function Home() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const toggleSidebar = () => setIsSidebarOpen(prev => !prev);
return (
    <Keyboard keys = {keys} onToggleSidebar={toggleSidebar}/>
  );
};
