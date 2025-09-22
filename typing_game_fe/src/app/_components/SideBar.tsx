"use client";

import styled from "styled-components";
import React, { useState, useRef, useContext } from "react";
import axios from "axios";
import { LongText } from "@/app/types/long-text";
import { ItemContext } from "@/app/(typing)/long/ItemContext";

interface SidebarProps {
  lyricsList: LongText[];
  isLoggedIn: boolean;
}

const Sidebar: React.FC<SidebarProps> = ({lyricsList, isLoggedIn}) => {
  const { selectedText, setSelectedText } = useContext(ItemContext);
  const [showUpload, setShowUpload] = useState(false);
  const [fileName, setFileName] = useState<string | null>(null);
  const [fileContent, setFileContent] = useState("");
  const [error, setError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);
  
  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setError(null);
    const file = e.target.files?.[0];
    if (!file) return;
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
    if (!fileContent.trim() || !fileName?.trim()) {
      setError("내용이 비어 있습니다.");
      return;
    }

    const baseTitle =
      (fileName?.replace(/\.[^.]+$/, "") || `내 파일 ${fileName.length + 1}`).trim() ||
      `내 파일 ${fileName.length + 1}`;

    const baseUrl = process.env.NEXT_PUBLIC_API_URL;
    axios.post(
      `${baseUrl}/my-long-text`,
      {
        title: baseTitle,
        content: (fileContent || ""),
      },
      {
        withCredentials: true
      }
    )
    .then(res => {
      console.log('나의 파일', res.data);
      window.location.reload();
    })
    .catch(error => {
      console.error(error);
    });

    setShowUpload(false);
    setFileName(null);
    setFileContent("");

  };

  const handleDelete = (longTextId: number) => {
    if (!confirm("정말 이 파일을 삭제하시겠습니까?")) return;
    const baseUrl = process.env.NEXT_PUBLIC_API_URL;

    axios.delete(`${baseUrl}/my-long-text/${longTextId}`, {
      withCredentials: true,
    })
    .then(res => {
      console.log("삭제 성공")
      window.location.reload();
    })
    .catch(err => {
      console.error(err);
    });
  };


  return (
  <Aside>
    <ContentWrapper>
      <div className="list">
        {[...lyricsList].map((song, index) => {
          const title = song.title;
          const longTextId = song.longTextId;
          const isSelected =
                selectedText?.longTextId === longTextId &&
                selectedText?.title === title;
          
          const isFirstUserFile = song.isUserFile && !lyricsList.slice(0, index).some(s => s.isUserFile);
          const isLastUserFile = song.isUserFile && !lyricsList.slice(index + 1).some(s => s.isUserFile);

          return (
            <React.Fragment key={`${index}`}>
              {isFirstUserFile  && <Divider/>}
              <a
                data-active={isSelected}
                onClick={() => setSelectedText(song)}
              >
                {title}
              </a>
              {(isLastUserFile && selectedText?.isUserFile) && (
                <ButtonDelete onClick={() => handleDelete(song.longTextId)}>삭제하기</ButtonDelete>
              )}
            </React.Fragment>
          );
        })}
      </div>
      <UploadToggleBtn onClick={() => setShowUpload(s => !s)}>
        {showUpload ? 'x' : '+add'}
      </UploadToggleBtn>
      {showUpload && (
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
          <ButtonRow>
            <UploadBtn onClick={handleUpload}>업로드</UploadBtn>
          </ButtonRow>
          <input
            type="text"
            onChange={e => setFileName(e.target.value)}
            style={{ width: "100%" , color: "black", height : "25px"}}
            placeholder="제목"
          />
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

        </UploadBox>
      )}
    </ContentWrapper>
  </Aside>
  );
};


export default Sidebar;


const Aside = styled.aside`
  position: fixed;
  top: 0;
  left: 0;
  width: 240px;
  height: 100vh;

  z-index: 5;
  padding: 10px;

  @media (max-width: 1100px) {
    display:none;
  }
`;

const ContentWrapper = styled.div`
  .list {
    display: block;
    max-height: calc(100vh - 200px);
    overflow-y: auto;
    
    scrollbar-width: none;
    -ms-overflow-style: none;


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
      width : 150px;
      overflow: hidden;

      &:hover {
        color: var(--color-correct);
      }
      &[data-active="true"] {
        color: black;
        :hover {
          background-color: #ededed;
        }
      }
    }
  }
`


const Divider = styled.hr`
  margin: 1rem 1rem 0.5rem;
  width : 120px;

  border: 0;
  border-top: 1px dashed #8c8c8c;
  border-bottom: 1px dashed #d8d3d3ff;

`;

const UploadToggleBtn = styled.button`
  color: #000000ff;
  margin: 10px auto 0.5rem auto; 
  cursor:pointer;
  padding-left: 1rem;
  cursor: pointer;
  font-size: 0.75rem;
  &:hover {
   color: var(--progress-fill);
  }
`;

const ButtonDelete = styled.button`
  margin: 1rem 1rem 0.5rem;
  color: #000000ff;
  cursor:pointer;
  cursor: pointer;
  font-size: 0.75rem;
  &:hover {
   color: var(--progress-fill);
  }
`;

const UploadBox = styled.div`
  width : 80%;
  margin: 0 1rem;
  padding : 1rem;
  border-radius: 0.5rem;
  font-size: 0.875rem;
  border : 1px solid grey;
  textarea{
    scrollbar-width: none;
    -ms-overflow-style: none;
    ::-webkit-scrollbar {
      display: none;
    }
  }
`;

const FileLabel = styled.label`
  display: block;
  width: 100%;
  text-align: center;
  cursor: pointer;
  padding: 0.25rem 0;
  border-radius: 1rem;
  margin-bottom: 0.5rem;

  &:hover {
    background-color: var(--progress-fill);
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
  margin-bottom : 10px;
`;

const UploadBtn = styled.button`
  flex: 1;
  background-color: var(--progress-fill);
  color: white;
  padding: 0.25rem 0;
  border-radius: 0.375rem;

  &:hover {
    background-color: var(--color-correct);
  }
  `;
