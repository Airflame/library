import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { AvailableBook } from 'src/app/model/available-book';
import { BookDataService } from '../../book-data.service';

@Component({
  selector: 'app-delete-book-dialog',
  templateUrl: './delete-book-dialog.component.html',
  styleUrls: ['./delete-book-dialog.component.scss'],
})
export class DeleteBookDialogComponent implements OnInit {
  public book: AvailableBook;

  constructor(public modal: NgbActiveModal, private bookDataService: BookDataService) {}

  ngOnInit(): void {}

  public deleteBook(): void {
    this.bookDataService.deleteBook(this.book.id);
  }
}
