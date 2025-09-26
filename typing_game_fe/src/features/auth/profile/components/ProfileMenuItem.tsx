import React from "react";
import Link from "next/link";

interface MenuItemProps {
  icon: React.ReactNode;
  label: string;
  onClick?: () => void;
  href?: string;
}

export const ProfileMenuItem: React.FC<MenuItemProps> = ({ icon, label, onClick, href }) => {
  const content = (
    <li
      className="flex items-center gap-2 px-3 py-1.5 rounded-lg text-sm cursor-pointer hover:bg-[var(--sub-menu-active)] transition-colors"
      onClick={onClick}
    >
      {icon} {label}
    </li>
  );

  return href ? <Link href={href}>{content}</Link> : content;
};
