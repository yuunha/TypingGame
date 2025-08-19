export interface KeyItem {
  code: string;
  label?: string;
  color?: 'blue' | 'red';
  widthLevel?: 0 | 1 | 2 | 3;
  href?: string;
}

export type Keys = KeyItem[][];