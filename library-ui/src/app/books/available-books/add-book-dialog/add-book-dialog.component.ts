import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Category } from 'src/app/model/category';
import { BookDataService } from '../../book-data.service';

@Component({
  selector: 'app-add-book-dialog',
  templateUrl: './add-book-dialog.component.html',
  styleUrls: ['./add-book-dialog.component.scss'],
})
export class AddBookDialogComponent implements OnInit {
  public categories: Category[];
  formGroup: FormGroup;
  selectedCategory: string = 'Default';

  constructor(
    public modal: NgbActiveModal,
    private bookDataService: BookDataService,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      author: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
      ]),
      title: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
      ]),
      category: new FormControl('', [
        Validators.maxLength(50),
      ]),
    });
    this.formGroup.get('category').setValue(this.selectedCategory);
  }

  save(): void {
    this.bookDataService.createBook(
      this.formGroup.controls['author'].value,
      this.formGroup.controls['title'].value,
      this.formGroup.controls['category'].value
    );
  }
}
