export interface Constitution {
  articleIndex: number;
  articleNumber: number;
  chapter: string;
  content: string;
  section: string | null;
  subsection: string | null;
}