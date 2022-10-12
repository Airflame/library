import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { merge, Observable } from 'rxjs';
import { CategoryDataService } from 'src/app/categories/category-data.service';
import { LendingDataService } from 'src/app/lendings/lending-data.service';
import { AvailableBook } from 'src/app/model/available-book';
import { Category } from 'src/app/model/category';
import { BookDataService } from '../book-data.service';
import { AddBookDialogComponent } from './add-book-dialog/add-book-dialog.component';
import { AvailableBooksService } from './available-books.service';
import { DeleteBookDialogComponent } from './delete-book-dialog/delete-book-dialog.component';
import { LendBookDialogComponent } from './lend-book-dialog/lend-book-dialog.component';
import { NgbdSortableHeader, SortEvent } from './sortable.directive';

@Component({
  selector: 'app-available-books',
  templateUrl: './available-books.component.html',
  styleUrls: ['./available-books.component.scss'],
})
export class AvailableBooksComponent implements OnInit {
  books$: Observable<AvailableBook[]>;
  total$: Observable<number>;
  categories: Category[];

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader>;

  constructor(
    public availableBooksService: AvailableBooksService,
    private modalService: NgbModal,
    private bookDataService: BookDataService,
    private categoryDataService: CategoryDataService,
    private lendingDataService: LendingDataService
  ) {}

  ngOnInit(): void {
    this.books$ = this.availableBooksService.books$;
    this.total$ = this.availableBooksService.total$;
    this.categoryDataService.getCategories().subscribe((result) => {
      this.categories = result;
    });
    merge(
      this.bookDataService.refresh$,
      this.categoryDataService.refresh$,
      this.lendingDataService.refresh$
    ).subscribe((v) => {
      this.availableBooksService.refresh();
    });
  }

  onSort({ column, direction }: SortEvent) {
    this.headers.forEach((header) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });
    this.availableBooksService.sortColumn = column;
    this.availableBooksService.sortDirection = direction;
  }

  addBook(): void {
    const modalRef = this.modalService.open(AddBookDialogComponent);
    modalRef.componentInstance.categories = this.categories;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }

  deleteBook(book: AvailableBook): void {
    const modalRef = this.modalService.open(DeleteBookDialogComponent);
    modalRef.componentInstance.book = book;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }

  lendBook(book: AvailableBook): void {
    const modalRef = this.modalService.open(LendBookDialogComponent);
    modalRef.componentInstance.book = book;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }
}
