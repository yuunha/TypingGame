'use client';

import React from "react";
import '../app/globals.css';

import Keyboard from './_components/Keyboard';
import keys from './_components/keyboard/keys'

export default function Home() {
return (
    <Keyboard keys = {keys} />
  );
};
