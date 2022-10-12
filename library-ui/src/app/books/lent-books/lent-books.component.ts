import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { merge, Observable } from 'rxjs';
import { CategoryDataService } from 'src/app/categories/category-data.service';
import { LendingDataService } from 'src/app/lendings/lending-data.service';
import { LentBook } from 'src/app/model/lent-book';
import {
  NgbdSortableHeader,
  SortEvent,
} from '../available-books/sortable.directive';
import { BookDataService } from '../book-data.service';
import { LentBooksService } from './lent-books.service';
import { ReturnBookDialogComponent } from './return-book-dialog/return-book-dialog.component';

@Component({
  selector: 'app-lent-books',
  templateUrl: './lent-books.component.html',
  styleUrls: ['./lent-books.component.scss'],
})
export class LentBooksComponent implements OnInit {
  books$: Observable<LentBook[]>;
  total$: Observable<number>;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader>;

  constructor(
    public lentBooksService: LentBooksService,
    private modalService: NgbModal,
    private bookDataService: BookDataService,
    private categoryDataService: CategoryDataService,
    private lendingDataService: LendingDataService
  ) {}

  ngOnInit(): void {
    this.books$ = this.lentBooksService.books$;
    this.total$ = this.lentBooksService.total$;
    merge(
      this.bookDataService.refresh$,
      this.categoryDataService.refresh$,
      this.lendingDataService.refresh$
    ).subscribe((v) => {
      this.lentBooksService.refresh();
    });
  }

  onSort({ column, direction }: SortEvent) {
    this.headers.forEach((header) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });
    this.lentBooksService.sortColumn = column;
    this.lentBooksService.sortDirection = direction;
  }

  returnBook(book: LentBook) {
    const modalRef = this.modalService.open(ReturnBookDialogComponent);
    modalRef.componentInstance.book = book;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }
}
