import "./globals.css";
import NavHeader from "@/app/_components/Header/NavHeader";

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body>
        <NavHeader /> 
        <div className="mainWrapper">{children}</div>
      </body>
    </html>
  );
}
