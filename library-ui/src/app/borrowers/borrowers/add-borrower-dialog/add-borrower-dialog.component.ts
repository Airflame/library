import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Borrower } from 'src/app/model/borrower';
import { BorrowerDataService } from '../../borrower-data.service';

@Component({
  selector: 'app-add-borrower-dialog',
  templateUrl: './add-borrower-dialog.component.html',
  styleUrls: ['./add-borrower-dialog.component.scss'],
})
export class AddBorrowerDialogComponent implements OnInit {
  formGroup: FormGroup;

  constructor(
    public modal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private borrowerDataService: BorrowerDataService
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
    });
  }

  save(): void {
    this.borrowerDataService.createBorrower(
      this.formGroup.getRawValue() as Borrower
    );
  }
}
