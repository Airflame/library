import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { CategoryEntity } from 'src/app/model/category-entity';
import { CategoryDataService } from '../../category-data.service';

@Component({
  selector: 'app-add-category-dialog',
  templateUrl: './add-category-dialog.component.html',
  styleUrls: ['./add-category-dialog.component.scss'],
})
export class AddCategoryDialogComponent implements OnInit {
  formGroup: FormGroup;

  constructor(
    public modal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private categoryDataService: CategoryDataService
  ) {}

  ngOnInit(): void {
    this.formGroup = this.formBuilder.group({
      name: new FormControl('', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(50),
      ]),
    });
  }

  save(): void {
    this.categoryDataService.createCategory(this.formGroup.getRawValue() as CategoryEntity);
  }
}
