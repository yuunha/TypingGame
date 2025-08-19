interface KeyItem {
  code: string;
  label?: string;
  color?: 'blue' | 'red';
  widthLevel?: 0 | 1 | 2 | 3;
  href?: string;
}

type MiniKeys = KeyItem[][];

const authKeys: MiniKeys = [
  [
    { code: "ControlLeft", label: "Home", color: 'blue', widthLevel: 2, href: '/' },
    { code: "KeyF", label: "친구", href: '/friend' },
    { code: "KeyC", label: "프로필㋛", href: '/profile' },
    { code: "KeyV", label: "로그인㋛", href: '/login' },
    { code: "Escape", label: "회원가입", color: 'red', href: '/signup' },
  ],
];

export default authKeys;