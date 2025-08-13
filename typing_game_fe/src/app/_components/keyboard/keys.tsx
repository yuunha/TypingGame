
const keys = [
  [
    { code: "Escape", label: "Esc", color: 'red' },
    { code: "KeyQ", label: "☀" },
    { code: "KeyW", label: "☁" },
    { code: "KeyE", label: "사" },
    { code: "KeyR", label: "이" },
    { code: "KeyU", label: "트" },
    { code: "KeyI", label: "☂" },
    { code: "KeyO", label: "☃" },
    { code: "KeyP", label: "♧" },
    { code: "Home", label: "Home", color: 'blue', widthLevel: 2, href: '/'},
  ],
  [
    { code: "CapsLock", label: "Caps", color: 'blue', widthLevel: 2 },
    { code: "KeyA", label: "긴" , href: '/long'},
    { code: "KeyS", label: "짧" , href: '/short'},
    { code: "KeyD", label: "낱" , href: '/word'},
    { code: "KeyF", label: "랭✧" , href: '/rank'},
    { code: "KeyJ", label: "🜸" },
    { code: "KeyK", label: "𐂂" },
    { code: "KeyL", label: "❤︎" },
    { code: "Backspace", label: "Backspace⌫", widthLevel: 0 },
  ],
  [
    { code: "ShiftLeft", label: "⇧ Shift", color: 'blue', widthLevel: 3 },
    { code: "KeyZ", label: "" },
    { code: "KeyX", label: "" },
    { code: "KeyC", label: "☾" },
    { code: "KeyV", label: "프로필㋛", href: '/profile' },
    { code: "Period", label: "로그인㋛", href: '/login'},
    { code: "Slash", label: "가입㋛",href: '/signup' },
    { code: "Enter", label: "⏎ Enter", color: 'red', widthLevel: 0 },
  ],
  [
    { code: "ControlLeft", label: "Ctrl", color: 'blue', widthLevel: 1 },
    { code: "MetaLeft", label: "🪟", color: 'blue' },
    { code: "AltLeft", label: "Alt", color: 'blue' },
    { code: "Space", label: "", widthLevel: 0 },
    { code: "Lang1", label: "한/영", color: 'blue', widthLevel: 1 },
    { code: "ArrowLeft", label: "<", color: 'blue' },
    { code: "ArrowRight", label: ">", color: 'blue' },
  ],
];

export default keys;

