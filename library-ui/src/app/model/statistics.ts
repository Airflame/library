import { CategoryBookCount } from "./category-book-count";
import { LendingTimeline } from "./lending-timeline";

export class Statistics {
    availableBooks: number;
    lentBooks: number;
    lendingTimeline: LendingTimeline;
    categories: CategoryBookCount[];
}