import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Category } from 'src/app/model/category';
import { CategoryDataService } from '../../category-data.service';

@Component({
  selector: 'app-delete-category-dialog',
  templateUrl: './delete-category-dialog.component.html',
  styleUrls: ['./delete-category-dialog.component.scss']
})
export class DeleteCategoryDialogComponent implements OnInit {
  public category: Category;

  constructor(public modal: NgbActiveModal, private categoryDataService: CategoryDataService) {}

  ngOnInit(): void {}

  public deleteCategory(): void {
    this.categoryDataService.deleteCategory(this.category.id);
  }

}
