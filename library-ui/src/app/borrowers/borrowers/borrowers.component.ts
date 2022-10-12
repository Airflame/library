import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { NgbdSortableHeader } from 'src/app/books/available-books/sortable.directive';
import { Borrower } from 'src/app/model/borrower';
import { BorrowerDataService } from '../borrower-data.service';
import { AddBorrowerDialogComponent } from './add-borrower-dialog/add-borrower-dialog.component';
import { BorrowersService } from './borrowers.service';
import { DeleteBorrowerDialogComponent } from './delete-borrower-dialog/delete-borrower-dialog.component';
import { SortEvent } from './sortable.directive';

@Component({
  selector: 'app-borrowers',
  templateUrl: './borrowers.component.html',
  styleUrls: ['./borrowers.component.scss'],
})
export class BorrowersComponent implements OnInit {
  borrowers$: Observable<Borrower[]>;
  total$: Observable<number>;
  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader>;

  constructor(
    public borrowersService: BorrowersService,
    private borrowerDataService: BorrowerDataService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.borrowers$ = this.borrowersService.borrowers$;
    this.total$ = this.borrowersService.total$;
    this.borrowerDataService.refresh$.subscribe((v) => {
      this.borrowersService.refresh();
    })
  }

  onSort({ column, direction }: SortEvent) {
    this.headers.forEach((header) => {
      if (header.sortable !== column) {
        header.direction = '';
      }
    });
    this.borrowersService.sortColumn = column;
    this.borrowersService.sortDirection = direction;
  }

  addBorrower(): void {
    const modalRef = this.modalService.open(AddBorrowerDialogComponent);
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }

  deleteBorrower(borrower: Borrower) {
    const modalRef = this.modalService.open(DeleteBorrowerDialogComponent);
    modalRef.componentInstance.borrower = borrower;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }
}
