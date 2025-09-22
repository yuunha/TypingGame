"use client";
import { createContext } from "react";
import { LongText } from "@/app/types/long-text";

export const ItemContext = createContext<{
  selectedText: LongText | null;
  setSelectedText: React.Dispatch<React.SetStateAction<LongText | null>>;
}>({
  selectedText: null,
  setSelectedText: () => {},
});
