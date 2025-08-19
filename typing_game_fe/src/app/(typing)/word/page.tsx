const Page = () => <div>준비 중...</div>;
export default Page;

// "use client";
// import React, { useState } from "react";
// import { wordList } from "../../../data/word"; // 여러 가사 목록 데이터
// import TypingGame from "../../_components/TypingGame";
// import Keyboard from "../../_components/Keyboard";
// import typingKeys from "../../_components/keyboard/typingKeys";

// import styled from "styled-components";

// const TypingPage: React.FC = () => {
//   const [selectedSong, setSelectedSong] = useState(wordList[0]);
//   return (
//     <Box>
//       <Content>
//         <Header>
//           <span>낱말연습</span>
//           <Title>{selectedSong.title}</Title>
//           <RightInfo>로그인</RightInfo>
//         </Header>

//         <Keyboard keys={typingKeys} />

//         <MainWrapper>
//           <TypingGame lyrics={selectedSong.lyrics} />
//         </MainWrapper>
//       </Content>
//     </Box>
//   );
// };

// export default TypingPage;



// const Box = styled.div`
//   position: relative;
//   display: flex;
//   justify-content: center;
//   align-items: center;
//   height: 100vh;
//   width: 100vw;
//   overflow: hidden;
// `;

// const Content = styled.div`
//   display: flex;
//   flex-direction: column;
//   align-items: center;
// `;

// const Header = styled.div`
//   display: flex;
//   justify-content: space-between;
//   align-items: center;
//   width: 100%;
//   color: black;
//   font-size: 0.8rem;
//   margin-top: 3rem;
// `;

// const Title = styled.h1`
//   font-size: 2.5rem;
//   font-weight: bold;
//   margin-bottom: 1rem;
//   text-align: center;
// `;

// const RightInfo = styled.div`
//   display: flex;
//   align-items: center;
//   gap: 1rem;
//   z-index: 1;
// `;

// const MainWrapper = styled.div`
//   width: 600px;
//   color: black;
//   border-radius: 0 20px 20px 0;
// `;
