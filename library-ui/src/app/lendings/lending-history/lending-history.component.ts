import { Component, OnInit, ViewChild } from '@angular/core';
import { merge, Observable, OperatorFunction, Subject } from 'rxjs';
import { SortDirection } from './sortable.directive';
import { Lending } from 'src/app/model/lending';
import { CurrentLendingsService } from './current-lendings.service';
import { PastLendingsService } from './past-lendings.service';
import { DeletePastLendingDialogComponent } from './delete-past-lending-dialog/delete-past-lending-dialog.component';
import { NgbModal, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { LendingDataService } from '../lending-data.service';
import { AvailableBook } from 'src/app/model/available-book';
import { CurrentBooksService } from './current-books.service';
import { ActiveToast, ToastrService } from 'ngx-toastr';
import { debounceTime, distinctUntilChanged, filter, map, take } from 'rxjs/operators';
import { LentBook } from 'src/app/model/lent-book';
import { ReturnBookDialogComponent } from 'src/app/books/lent-books/return-book-dialog/return-book-dialog.component';
import { BorrowerDataService } from 'src/app/borrowers/borrower-data.service';

@Component({
  selector: 'app-lending-history',
  templateUrl: './lending-history.component.html',
  styleUrls: ['./lending-history.component.scss'],
})
export class LendingHistoryComponent implements OnInit {
  borrowerInput: string;
  borrowerHeader: string;
  borrowers: string[];

  currentLendings$: Observable<Lending[]>;
  currentTotal$: Observable<number>;
  showCurrent$: Observable<boolean>;
  pastLendings$: Observable<Lending[]>;
  pastTotal$: Observable<number>;
  showPastLendings$: Observable<boolean>;
  currentBooks$: Observable<AvailableBook[]>;
  currentBooksTotal$: Observable<number>;

  currentColumn: string;
  currentDirection: SortDirection;
  pastColumn: string;
  pastDirection: SortDirection;
  bookColumn: string;
  bookDirection: SortDirection;

  private errorToast: ActiveToast<any>;
  reloadPage$ = new Subject<number>();

  @ViewChild('instance', { static: true }) instance: NgbTypeahead;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  constructor(
    private modalService: NgbModal,
    public pastLendingsService: PastLendingsService,
    public currentLendingsService: CurrentLendingsService,
    public currentBooksService: CurrentBooksService,
    private lendingDataService: LendingDataService,
    private borrowerDataService: BorrowerDataService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.currentLendings$ = this.currentLendingsService.lendings$;
    this.currentTotal$ = this.currentLendingsService.total$;
    this.showCurrent$ = this.currentLendingsService.show$;
    this.pastLendings$ = this.pastLendingsService.lendings$;
    this.pastTotal$ = this.pastLendingsService.total$;
    this.showPastLendings$ = this.pastLendingsService.show$;
    this.currentBooks$ = this.currentBooksService.books$;
    this.currentBooksTotal$ = this.currentBooksService.total$;
    this.lendingDataService.refresh$.subscribe((v) => {
      this.currentLendingsService.hide();
      this.pastLendingsService.hide();
      this.borrowerHeader = null;
    });
    this.borrowerDataService.getAllBorrowers().subscribe((result) => {
      this.borrowers = result
        .sort((a, b) => (a.lastName > b.lastName ? 1 : -1))
        .map((p) => p.firstName + ' ' + p.lastName);
    });
    this.reloadPage$.pipe(debounceTime(100)).subscribe(v => this.selectBorrower());
  }

  searchBorrower: OperatorFunction<string, readonly string[]> = (
    text$: Observable<string>
  ) => {
    const debouncedText$ = text$.pipe(distinctUntilChanged());
    const clicksWithClosedPopup$ = this.click$.pipe(
      filter(() => !this.instance.isPopupOpen())
    );
    const inputFocus$ = this.focus$;

    return merge(debouncedText$, inputFocus$, clicksWithClosedPopup$).pipe(
      map((term) =>
        (term === ''
          ? this.borrowers
          : this.borrowers.filter(
              (p) => p.toLowerCase().indexOf(term.toLowerCase()) > -1
            )
        ).slice(0, 10)
      )
    );
  };

  selectBorrower() {
    if (this.borrowerInput != null) {
      let split = this.borrowerInput.split(' ');
      this.lendingDataService.getLendingHistory(split[0], split[1]).subscribe(
        (result) => {
          this.borrowerHeader = this.borrowerInput;
          if (this.errorToast != null)
            this.toastrService.clear(this.errorToast.toastId);
          this.pastLendingsService.refresh(result);
          this.currentLendingsService.refresh(result);
          this.currentBooksService.refresh(result);
        },
        (error) => {
          this.borrowerHeader = null;
          this.currentLendingsService.hide();
          this.pastLendingsService.hide();
          this.errorToast = this.toastrService.error(
            error.error.message,
            'Error ' + error.status.toString()
          );
        }
      );
    }
  }

  currentOnSort({ column, direction }) {
    this.currentColumn = column;
    this.currentDirection = direction;
    this.currentLendingsService.sortColumn = column;
    this.currentLendingsService.sortDirection = direction;
  }

  pastOnSort({ column, direction }) {
    this.pastColumn = column;
    this.pastDirection = direction;
    this.pastLendingsService.sortColumn = column;
    this.pastLendingsService.sortDirection = direction;
  }

  booksOnSort({ column, direction }) {
    this.bookColumn = column;
    this.bookDirection = direction;
    this.currentBooksService.sortColumn = column;
    this.currentBooksService.sortDirection = direction;
  }

  deletePastLending(pastLending: Lending): void {
    const modalRef = this.modalService.open(DeletePastLendingDialogComponent);
    modalRef.componentInstance.pastLending = pastLending;
    modalRef.result.then(
      (val) => {
        this.reloadPage$.next();
      },
      (error) => {}
    );
  }

  returnBook(book: AvailableBook) {
    const lentBook: LentBook = new LentBook();
    lentBook.id = book.id;
    this.currentLendings$.pipe(take(1)).subscribe(lendings => {
      lentBook.dateLent = lendings.find(l => l.bookId == lentBook.id).dateLent;
    });
    const modalRef = this.modalService.open(ReturnBookDialogComponent);
    modalRef.componentInstance.book = lentBook;
    modalRef.result.then(
      (val) => {
        this.reloadPage$.next();
      },
      (error) => {}
    );
  }
}
