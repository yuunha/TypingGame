"use client";

import React, { useState, useRef } from "react";

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
    reader.onload = ev => {
      const raw = (ev.target?.result as string) ?? "";
      setFileContent(raw);
    };
    reader.onerror = () => {
      setError("파일을 읽는 중 오류가 발생했습니다.");
    };
    reader.readAsText(file, "EUC-KR");
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
    <aside className="w-64 min-w-[16rem] flex-shrink-0 bg-gray-100 border-r p-4 overflow-y-auto">
      <h2 className="text-xl font-bold mb-4">가사 목록</h2>

      {/* 기본 제공 글 */}
      <ul>
        {lyricsList.map((song, index) => (
          <li
            key={`default-${index}`}
            onClick={() => onSelectSong(song)}
            className={`p-2 rounded cursor-pointer mb-2 ${
              selectedSong.title === song.title
                ? "bg-blue-500 text-white"
                : "hover:bg-gray-200"
            }`}
          >
            {song.title}
          </li>
        ))}
      </ul>

      {/* 내 파일 목록 */}
      {uploadedFiles.length > 0 && (
        <>
          <h3 className="text-lg font-semibold mt-6 mb-2">내 파일</h3>
          <ul>
            {uploadedFiles.map((file, index) => (
              <li
                key={`uploaded-${index}`}
                onClick={() => onSelectSong(file)}
                className={`p-2 rounded cursor-pointer mb-2 ${
                  selectedSong.title === file.title
                    ? "bg-blue-500 text-white"
                    : "hover:bg-gray-200"
                }`}
              >
                {file.title}
              </li>
            ))}
          </ul>
        </>
      )}

      {/* 플러스 버튼 */}
      <button
        type="button"
        onClick={() => setShowUpload(s => !s)}
        className="mt-4 w-full bg-green-500 text-white py-2 rounded hover:bg-green-600"
      >
        {showUpload ? "닫기" : "+ 파일 업로드"}
      </button>

      {/* 업로드 UI */}
      {showUpload && (
        <div className="mt-4 p-3 border rounded bg-white text-sm">
          <label
            htmlFor="sidebar-file-input"
            className="block w-full text-center cursor-pointer bg-gray-200 hover:bg-gray-300 py-1 rounded mb-2"
          >
            {fileName ? `선택됨: ${fileName}` : "TXT 파일 선택"}
          </label>
          <input
            id="sidebar-file-input"
            ref={fileInputRef}
            type="file"
            accept=".txt"
            onChange={handleFileChange}
            className="hidden"
          />

          {fileContent && (
            <p className="text-xs text-gray-600 mb-2">
              줄 수: {fileContent.split(/\r?\n/).length}
            </p>
          )}

          {error && <p className="text-xs text-red-500 mb-2">{error}</p>}

          <div className="flex gap-2">
            <button
              type="button"
              onClick={handleUpload}
              className="flex-1 bg-blue-500 text-white py-1 rounded hover:bg-blue-600"
            >
              업로드
            </button>
            <button
              type="button"
              onClick={handleCancelUpload}
              className="flex-1 bg-gray-300 text-gray-800 py-1 rounded hover:bg-gray-400"
            >
              취소
            </button>
          </div>
        </div>
      )}
    </aside>
  );
};

export default Sidebar;
