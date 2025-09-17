export interface Constitution {
  articleIndex: number;
  articleNumber: string;
  chapter: string;
  content: string;
  section: string | null;
  subsection: string | null;
}