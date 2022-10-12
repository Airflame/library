import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import moment from 'moment';
import { LendingDataService } from 'src/app/lendings/lending-data.service';
import { LentBook } from 'src/app/model/lent-book';

@Component({
  selector: 'app-return-book-dialog',
  templateUrl: './return-book-dialog.component.html',
  styleUrls: ['./return-book-dialog.component.scss'],
})
export class ReturnBookDialogComponent implements OnInit {
  invalidDate: boolean;
  dateModel: NgbDateStruct;
  book: LentBook;

  constructor(
    public modal: NgbActiveModal,
    private lendingDataService: LendingDataService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {}

  validateDate(): void {
    let dateReturned = moment();
    if (this.dateModel)
      dateReturned = moment(
        this.dateModel.year +
          '-' +
          this.dateModel.month +
          '-' +
          this.dateModel.day
      );
    if (dateReturned.isBefore(this.book.dateLent))
        this.invalidDate = true;
    else
        this.invalidDate = false;
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
    this.lendingDataService.returnBook(this.book.id, date);
  }
}
