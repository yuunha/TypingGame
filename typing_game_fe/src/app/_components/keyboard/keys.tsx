
const keys = [
  [
    { code: "Escape", label: "Esc", color: 'red' },
    { code: "KeyQ", label: "" },
    { code: "KeyW", label: "" },
    { code: "KeyE", label: "" },
    { code: "KeyR", label: "" },
    { code: "KeyU", label: "" },
    { code: "KeyI", label: "" },
    { code: "KeyO", label: "" },
    { code: "KeyP", label: "❤︎" },
    { code: "Home", label: "Home", color: 'red', widthLevel: 2, href: '/'},
  ],
  [
    { code: "CapsLock", label: "Caps", widthLevel: 2 },
    { code: "KeyA", label: "ㅁ" , href: '/long'},
    { code: "KeyS", label: "ㄴ" , href: '/short'},
    { code: "KeyD", label: "ㅇ" , href: '/word'},
    { code: "KeyF", label: "ㄹ" , href: '/rank'},
    { code: "KeyJ", label: "ㅓ" },
    { code: "KeyK", label: "ㅏ" },
    { code: "KeyL", label: "ㅣ" },
    { code: "Backspace", label: "Backspace⌫", widthLevel: 0 },
  ],
  [
    { code: "ShiftLeft", label: "⇧ Shift", widthLevel: 3 },
    { code: "KeyZ", label: "" },
    { code: "KeyX", label: "" },
    { code: "KeyC", label: "" },
    { code: "KeyV", },
    { code: "Period", },
    { code: "Slash", label: "회원㋛", href: '/authmenu' },
    { code: "Enter", label: "⏎ Enter", widthLevel: 0 },
  ],
  [
    { code: "ControlLeft", label: "Ctrl", widthLevel: 1 },
    { code: "MetaLeft", label: "" },
    { code: "AltLeft", label: "Alt" },
    { code: "Space", label: "", widthLevel: 0 },
    { code: "Lang1", label: "한/영", widthLevel: 1 },
    { code: "ArrowLeft", label: "<" , color: 'red'},
    { code: "ArrowRight", label: ">" , color: 'red'},
  ],
];

export default keys;

