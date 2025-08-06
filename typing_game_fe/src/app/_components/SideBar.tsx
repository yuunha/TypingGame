"use client";

import styled from "styled-components";
import React, { useState, useRef } from "react";


import Widget from "../_components/Widget"

interface SongLike {
  title: string;
  lyrics: string[];
}

interface SidebarProps {
  lyricsList: SongLike[];                    // 기본 제공 글
  uploadedFiles: SongLike[];                 // 사용자 업로드 글
  selectedSong: SongLike;                    // 현재 선택된 글
  onSelectSong: (song: SongLike) => void;    // 글 선택 콜백
  onUploadFile: (file: SongLike) => void;    // 업로드 완료 콜백 (부모가 상태 저장)
}

const Sidebar: React.FC<SidebarProps> = ({
  lyricsList,
  uploadedFiles,
  selectedSong,
  onSelectSong,
  onUploadFile,
}) => {
  const [showUpload, setShowUpload] = useState(false);
  const [fileName, setFileName] = useState<string | null>(null);
  const [fileContent, setFileContent] = useState("");
  const [error, setError] = useState<string | null>(null);
  // 파일 input을 다시 선택할 수 있게 ref 사용 (같은 파일 재업로드 대비)
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  /** 파일 선택 핸들러 */
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError(null);
    const file = e.target.files?.[0];
    if (!file) {
      setFileName(null);
      setFileContent("");
      return;
    }

    setFileName(file.name);

    const reader = new FileReader();
    reader.onload = () => {
      try {
        const buffer = reader.result as ArrayBuffer;
        let text = "";

        text = new TextDecoder("utf-8").decode(buffer);

        setFileContent(text);
      } catch {
        setError("텍스트 디코딩에 실패했습니다.");
      }
    };

    reader.onerror = () => setError("파일을 읽는 중 오류가 발생했습니다.");
    reader.readAsArrayBuffer(file);
  };

  /** 업로드 확정 */
  const handleUpload = () => {
    if (!fileContent.trim()) {
      setError("내용이 비어 있습니다.");
      return;
    }

    // 줄 단위 파싱: 공백 줄 제거
    const lines = fileContent
      .split(/\r?\n/)
      .map(l => l.trim())
      .filter(l => l.length > 0);

    if (lines.length === 0) {
      setError("사용할 수 있는 줄이 없습니다.");
      return;
    }

    // 제목: 파일명(확장자 제거) 또는 자동카운트
    const baseTitle =
      (fileName?.replace(/\.[^.]+$/, "") || `내 파일 ${uploadedFiles.length + 1}`).trim() ||
      `내 파일 ${uploadedFiles.length + 1}`;

    const newFile: SongLike = {
      title: baseTitle,
      lyrics: lines,
    };

    // 부모에 전달
    onUploadFile(newFile);

    // 업로드 후 자동 선택
    onSelectSong(newFile);

    // UI 리셋
    setShowUpload(false);
    setFileName(null);
    setFileContent("");
    if (fileInputRef.current) {
      fileInputRef.current.value = ""; // 같은 파일 다시 선택 가능하게
    }
  };

  /** 업로드 취소 */
  const handleCancelUpload = () => {
    setShowUpload(false);
    setFileName(null);
    setFileContent("");
    setError(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
  };

  return (
  <Aside>
    <ContentWrapper>
      <div className="list">
      {lyricsList.map((song, index) => (
          <a
              key={`default-${index}`}
              data-active={selectedSong.title === song.title}
              onClick={() => onSelectSong(song)}
          >
              {song.title}
          </a>
          ))}
      </div>
      {uploadedFiles.length > 0 && (
      <>
        <SubHeading>내 파일</SubHeading>
        <List>
          {uploadedFiles.map((file, index) => (
            <ListItem
              key={`uploaded-${index}`}
              selected={selectedSong.title === file.title}
              onClick={() => onSelectSong(file)}
            >
              {file.title}
            </ListItem>
          ))}
        </List>
      </>
      )}
      {/* {showUpload && (
        <UploadBox>
          <FileLabel htmlFor="sidebar-file-input">
            {fileName ? `선택됨: ${fileName}` : 'TXT 파일 선택'}
          </FileLabel>
          <input
            id="sidebar-file-input"
            ref={fileInputRef}
            type="file"
            accept=".txt"
            onChange={handleFileChange}
            style={{ display: 'none' }}
          />
          
          <FileLabel htmlFor="paste-area">텍스트 붙여넣기</FileLabel>
          <textarea
            id="paste-area"
            value={fileContent}
            onChange={e => setFileContent(e.target.value)}
            rows={10}
            style={{ width: "100%" , color: "black", height : "40px"}}
            placeholder="여기에 텍스트를 붙여넣으세요"
          />
          
          {fileContent && (
            <LineCount>줄 수: {fileContent.split(/\r?\n/).length}</LineCount>
          )}
          {error && <ErrorMessage>{error}</ErrorMessage>}

          <ButtonRow>
            <UploadBtn onClick={handleUpload}>업로드</UploadBtn>
          </ButtonRow>
        </UploadBox>
      )}
      <UploadToggleBtn onClick={() => setShowUpload(s => !s)}>
        {showUpload ? '-' : '+'}
      </UploadToggleBtn> */}
    </ContentWrapper>
  </Aside>
  );
};


export default Sidebar;


const Aside = styled.aside`
  width: 13rem;
  min-width: 13rem;
  left :440px;
  top: 177px;
  position:relative;
  height :100px;
`;

const ContentWrapper = styled.div`
  .list {
    display: flex;
    overflow: scroll;
    height:300px;

    scrollbar-width: none;
    -ms-overflow-style: none;

    @media (min-width: 1024px) {
      display: block;
    }

    a {
      display: block;
      padding: 0.45rem;
      padding-left: 1rem;
      padding-right: 1rem;
      margin-top: 0.25rem;
      margin-bottom: 0.25rem;
      border-radius: 0.3rem;
      font-size: 0.875rem;
      line-height: 1.25rem;
      color: grey;
      flex-shrink: 0;
      cursor: pointer;

      &:hover {
        // background-color: #ededed;
        color: var(--color-correct);
      }
      &[data-active="true"] {
        color: black;
        // background-color: #ededed;

        :hover {
          background-color: #ededed;
        }
      }
    }
  }
`



const Heading = styled.h2`
  font-size: 1.25rem;
  font-weight: bold;
  margin-bottom: 1rem;
`;

const SubHeading = styled.h3`
  font-size: 1.125rem;
  font-weight: 600;
  margin: 1.5rem 0 0.5rem;
`;

const List = styled.ul`
  margin-bottom: 1rem;
`;

const ListItem = styled.li<{ selected: boolean }>`
  padding: 0.5rem;
  border-radius: 0.375rem;
  cursor: pointer;
  margin-bottom: 0.5rem;
  color: ${({ selected }) => (selected ? 'white' : 'inherit')};

  &:hover {
    color: ${({ selected }) => (selected ? 'var(--color-point)' : '#e5e7eb')};
  }
`;

const UploadToggleBtn = styled.button`
  width: 2.5rem;
  height: 2.5rem;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.6);
  color: white;
  font-size: 1.5rem;
  font-weight: bold;
  border: none;
  margin: 10px auto 0.5rem auto; 
  display: flex;
  align-items: center;
  justify-content: center;
  
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 0 0px 4px 0 rgba(0, 0, 0, 0.2);

  &:hover {
    background-color: #16a34a;
  }
`;

const UploadBox = styled.div`
  margin-top: 1rem;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 0.375rem;
  background-color: white;
  font-size: 0.875rem;
`;

const FileLabel = styled.label`
  display: block;
  width: 100%;
  text-align: center;
  cursor: pointer;
  background-color: #e5e7eb;
  padding: 0.25rem 0;
  border-radius: 0.375rem;
  margin-bottom: 0.5rem;

  &:hover {
    background-color: #d1d5db;
  }
`;

const LineCount = styled.p`
  font-size: 0.75rem;
  color: #4b5563;
  margin-bottom: 0.5rem;
`;

const ErrorMessage = styled.p`
  font-size: 0.75rem;
  color: #dc2626;
  margin-bottom: 0.5rem;
`;

const ButtonRow = styled.div`
  display: flex;
  gap: 0.5rem;
`;

const UploadBtn = styled.button`
  flex: 1;
  background-color: #3b82f6;
  color: white;
  padding: 0.25rem 0;
  border-radius: 0.375rem;

  &:hover {
    background-color: #2563eb;
  }
  `;
