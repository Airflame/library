import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Borrower } from 'src/app/model/borrower';
import { BorrowerDataService } from '../../borrower-data.service';

@Component({
  selector: 'app-delete-borrower-dialog',
  templateUrl: './delete-borrower-dialog.component.html',
  styleUrls: ['./delete-borrower-dialog.component.scss']
})
export class DeleteBorrowerDialogComponent implements OnInit {
  public borrower: Borrower;

  constructor(public modal: NgbActiveModal, private borrowerDataService: BorrowerDataService) { }

  ngOnInit(): void {
  }

  public deleteBorrower(): void {
    this.borrowerDataService.deleteBorrower(this.borrower.id);
  }

}
