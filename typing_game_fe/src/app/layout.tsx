'use client';

import "./globals.css";
import styled from "styled-components";

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko">
      <body>
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
  min-height:100vh;
  background-color : var(--background);
`;