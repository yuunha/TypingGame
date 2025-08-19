// "use client";
// import React, { useState, useEffect } from "react";
// import { lyricsList } from "../../../data/lyricsList"; // 여러 가사 목록 데이터

// import styled from "styled-components";
// import typingKeys from "@/app/_components/keyboard/typingKeys";
// import Keyboard from "@/app/_components/Keyboard";
// import Sidebar from "@/app/_components/SideBar";

// const RankPage: React.FC = () => {
//   const [selectedSong, setSelectedSong] = useState(lyricsList[0]);
//   const username = "admin";
//     const password = "12345";
//     const basicAuth = btoa(`${username}:${password}`);
//     useEffect(() => {
//     const fetchScore = async () => {
//         try {
//         const res = await fetch(`http://localhost:8080/long-text/score?title=${encodeURIComponent(selectedSong.title)}`, {
//             method: "GET",
//             headers: {
//                 "Authorization": `Basic ${basicAuth}`,
//                 "Content-Type": "application/json"
//             }
//         });

//         console.log("응답 상태:", res.status, "응답 OK?", res.ok);
//         console.log("선택된 글:", selectedSong.title)
//         const text = await res.text();
//         console.log(text);

//         if (res.status === 401) {
//             console.log("인증문제");
//             // 처리 로직
//         } else if (res.status === 403) {
//             console.log("인가문제.");
//         }
//         } catch (error) {
//             console.log("유효성문제");
//         }
//     };

//     fetchScore();
//     }, [selectedSong]);


//   const [uploadedFiles, setUploadedFiles] = useState<
//     { title: string; lyrics: string[] }[]
//   >([]);

//   const handleUploadFile = (file: { title: string; lyrics: string[] }) => {
//     setUploadedFiles((prev) => [...prev, file]);
//   };

//   const [isSidebarOpen, setIsSidebarOpen] = useState(true);
//   const toggleSidebar = () => setIsSidebarOpen(prev => !prev);
//   return (
//     <>
//       <Box>        
//       {isSidebarOpen && (
//         <SidebarWrapper>
//           <Sidebar
//             lyricsList={lyricsList}
//             uploadedFiles={uploadedFiles}
//             selectedSong={selectedSong}
//             onSelectSong={setSelectedSong}
//             onUploadFile={handleUploadFile}
//           />
//         </SidebarWrapper>
//       )}
//         <ContentWrapper>
//           <RankBoard>
//             랭킹 Board~
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//             <div>dd</div>
//           </RankBoard>
//           <Keyboard keys={typingKeys} onToggleSidebar={toggleSidebar} />
//         </ContentWrapper>
//       </Box>
//     </>
//   );
// }
// export default RankPage;

// const Box = styled.div`
//   position: relative;      
//   display: flex;
//   justify-content: center; 
//   align-items: center;
//   height: 100vh;
//   width: 100vw;
//   overflow: hidden;
// `;

// const SidebarWrapper = styled.div`
//   position: absolute;
//   left: 0;
//   top: 0;
// `;

// const ContentWrapper = styled.div`
//   display: flex;
//   flex-direction: column;
//   align-items: center;
//   justify-content: center;
// `;

// const RankBoard = styled.div`
//   margin-bottom: 1rem;
//   font-size: 1.5rem;
//   color: #333;
//   text-align: center;
//   height: 300px;
//   overflow:scroll;
//     scrollbar-width: none;
//     -ms-overflow-style: none;
//   margin-bottom :40px;
//   font-size: 1rem;
//   line-height: 2rem;
// `;

