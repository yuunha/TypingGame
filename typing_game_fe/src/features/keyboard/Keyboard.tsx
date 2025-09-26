import React from "react";
import '@/app/globals.css';
import KeyboardClient from "@/features/keyboard/KeyboardClient";
import { Keys } from '@/types/key-item';

interface KeyboardProps {
  keys: Keys;
  onToggleSidebar?: () => void;
}

export default function KeyboardFrame({ keys, onToggleSidebar }: KeyboardProps) {
  return <KeyboardClient keys={keys} onToggleSidebar={onToggleSidebar} />;
}