import { CategoryBookCount } from "./category-book-count";

export class Statistics {
    availableBooks: number;
    lentBooks: number;
    lendingsTimeline: Map<string, number>;
    categories: CategoryBookCount[];
}