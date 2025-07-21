import Image from "next/image";
import TypingGame from "../components/TypingPage";
import TypingPage from "../components/TypingPage";
import NavigationBar from "@/components/NavigationBar";

export default function Home() {
  return (
    <main className="flex min-h-screen flex-col items-center justify-center bg-gray-100">
      <NavigationBar/>
      <TypingPage/>
    </main>
  );
}
