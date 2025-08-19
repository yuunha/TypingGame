interface KeyItem {
  code: string;
  label?: string;
  color?: 'blue' | 'red';
  widthLevel?: 0 | 1 | 2 | 3;
  href?: string;
}

type Keys = KeyItem[][];


const miniKeys:Keys = [
  [
    { code: "ControlLeft", label: "Home", color: 'blue', widthLevel: 2, href: '/' },
    { code: "KeyC", label: "☾" },
    { code: "KeyV", label: "프로필㋛", href: '/profile' },
    { code: "Escape", label: "Esc", color: 'red' },
  ],
];


