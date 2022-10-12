import { Component, OnInit, ViewChild } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal, NgbDateStruct, NgbTypeahead } from '@ng-bootstrap/ng-bootstrap';
import { OperatorFunction, Observable, merge, Subject } from 'rxjs';
import { distinctUntilChanged, filter, map } from 'rxjs/operators';
import { BorrowerDataService } from 'src/app/borrowers/borrower-data.service';
import { LendingDataService } from 'src/app/lendings/lending-data.service';
import { AvailableBook } from 'src/app/model/available-book';
import { Borrower } from 'src/app/model/borrower';

@Component({
  selector: 'app-lend-book-dialog',
  templateUrl: './lend-book-dialog.component.html',
  styleUrls: ['./lend-book-dialog.component.scss'],
})
export class LendBookDialogComponent implements OnInit {
  formGroup: FormGroup;
  dateModel: NgbDateStruct;
  book: AvailableBook;
  borrowerInput: string;
  borrowers: string[];

  @ViewChild('instance', { static: true }) instance: NgbTypeahead;
  focus$ = new Subject<string>();
  click$ = new Subject<string>();

  constructor(
    public modal: NgbActiveModal,
    private lendingDataService: LendingDataService,
    private borrowerDataService: BorrowerDataService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      firstName: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
      ]),
      lastName: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
      ]),
      comment: new FormControl('', [Validators.maxLength(50)]),
    });
    this.borrowerDataService.getAllBorrowers().subscribe((result) => {
      this.borrowers = result
        .sort((a, b) => (a.lastName > b.lastName ? 1 : -1))
        .map((p) => p.firstName + ' ' + p.lastName);
    });
  }

  searchBorrower: OperatorFunction<string, readonly string[]> = (
    text$: Observable<string>
  ) => {
    if (this.borrowerInput != null) {
      let split = this.borrowerInput.split(' ');
      this.formGroup.get('firstName').setValue(split[0]);
      this.formGroup.get('lastName').setValue(split[1]);
    }
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
        ).slice(0, 5)
      )
    );
  };

  selectBorrower() {
    if (this.borrowerInput != null) {
      let split = this.borrowerInput.split(' ');
      this.formGroup.get('firstName').setValue(split[0]);
      this.formGroup.get('lastName').setValue(split[1]);
    }
  }

  save(): void {
    let date: string = null;
    if (this.dateModel != null)
      date =
        this.dateModel.year +
        '-' +
        ('0' + this.dateModel.month).slice(-2) +
        '-' +
        ('0' + this.dateModel.day).slice(-2);
    this.lendingDataService.lendBook(
      this.book.id,
      this.formGroup.controls['firstName'].value,
      this.formGroup.controls['lastName'].value,
      date,
      this.formGroup.controls['comment'].value
    );
  }
}
