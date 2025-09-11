import "./globals.css";
import NavHeader from "./_components/NavHeader";

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
