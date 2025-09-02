'use client';

import "./globals.css";
import styled from "styled-components";
import NavHeader from "./_components/NavHeader";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>
        <NavHeader></NavHeader>
        <Container>
          {children}
        </Container>
      </body>
    </html>
  );
}


const Container = styled.div`
  display:flex;
  align-items:center;
  justify-content: center;
  background-color : var(--background);
`;