import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Lending } from 'src/app/model/lending';
import { LendingDataService } from '../../lending-data.service';

@Component({
  selector: 'app-delete-past-lending-dialog',
  templateUrl: './delete-past-lending-dialog.component.html',
  styleUrls: ['./delete-past-lending-dialog.component.scss']
})
export class DeletePastLendingDialogComponent implements OnInit {
  pastLending: Lending;

  constructor(public modal: NgbActiveModal, private lendingDataService: LendingDataService) {}

  ngOnInit(): void {}

  public deletePastLending(): void {
    this.lendingDataService.deletePastLending(this.pastLending.id);
  }

}
