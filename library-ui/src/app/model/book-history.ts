import { Lending } from "./lending";

export class BookHistory {
  id: number;
  author: string;
  title: string;
  isLent: boolean;
  lendings: Lending[];
}
