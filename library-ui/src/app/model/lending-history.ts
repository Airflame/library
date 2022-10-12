import { AvailableBook } from "./available-book";
import { Lending } from "./lending";

export class LendingHistory {
  firstName: string;
  lastName: string;
  pastLendings: Lending[];
  currentLendings: Lending[];
  lentBooks: AvailableBook[];
  bookCount: number;
}
