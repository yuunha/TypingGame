'use client';

import React from "react";
import styled from "styled-components";
import Link from "next/link";
import { Keys } from "../types/key-item";


interface KeyboardProps {
  keys: Keys;
}

const KeyboardMini: React.FC<KeyboardProps> = ({ keys }) => {
  return (
    <MenuWrapper>
      {keys.map((row, rowIndex) => (
        <Row key={rowIndex}>
          {row.map(({ code, label, href }) =>
            href ? (
              <StyledLink key={code} href={href}>
                {label}
              </StyledLink>
            ) : (
              <MenuItem key={code}>{label}</MenuItem>
            )
          )}
        </Row>
      ))}
    </MenuWrapper>
  );
};

export default KeyboardMini;

const MenuWrapper = styled.div`
  display: flex;
  flex-direction: column;
  padding: 8px;
  gap: 6px;
`;

const Row = styled.div`
  display: flex;
  gap: 6px;
`;

const MenuItem = styled.div`
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  background: #f3f4f6;
  color: #111;
  cursor: default;
`;

const StyledLink = styled(Link)`
  padding: 6px 12px;
  border-radius: 6px;
  font-size: 14px;
  background: #e5e7eb;
  color: #111;
  text-decoration: none;

  &:hover {
    background: #d1d5db;
  }
`;