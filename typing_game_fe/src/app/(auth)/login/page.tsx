import React from "react";
import LoginForm from '@/features/auth/login/components/LoginForm'

export default function LoginPage() {
  return (
    <div className="flex flex-col pt-48">
      <div className="w-72 flex items-start gap-5">
        <div className="flex-1 min-w-0">
          <h1 className="mb-5 text-center text-2xl">계정 접속하기</h1>
          <LoginForm/>
          <div className="mt-1 text-right [color:var(--typing-line-sub)] text-[0.8rem]">
            <a href="/signup">새 계정 만들기</a>
          </div>
        </div>   
      </div>
    </div>
  );
};
