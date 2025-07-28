'use client';

import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import styled from "styled-components";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    
    <html lang="en">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
      <Container>
        <Background />
        {children}
      </Container>
      </body>
    </html>
  );
}


const Container = styled.div`
  position: relative;
  width: 100%;
  height: 100vh;
`;

const Background = styled.div`
  position: absolute;
  inset: 0;
  background-image: url('/b3.jpg');
  background-size: cover;
  background-position: center;
  background-color: white;
`;
