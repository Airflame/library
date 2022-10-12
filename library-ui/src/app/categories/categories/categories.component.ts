import { Component, OnInit, QueryList, ViewChildren } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { merge, Observable } from 'rxjs';
import { SortDirection } from 'src/app/books/available-books/sortable.directive';
import { BookDataService } from 'src/app/books/book-data.service';
import { Category } from 'src/app/model/category';
import { CategoryDataService } from '../category-data.service';
import { AddCategoryDialogComponent } from './add-category-dialog/add-category-dialog.component';
import { CategoriesService } from './categories.service';
import { DeleteCategoryDialogComponent } from './delete-category-dialog/delete-category-dialog.component';
import { NgbdSortableHeader, SortEvent } from './sortable.directive';

@Component({
  selector: 'app-categories',
  templateUrl: './categories.component.html',
  styleUrls: ['./categories.component.scss'],
})
export class CategoriesComponent implements OnInit {
  categories$: Observable<Category[]>;
  total$: Observable<number>;
  column: string;
  direction: SortDirection;

  @ViewChildren(NgbdSortableHeader) headers: QueryList<NgbdSortableHeader>;

  constructor(
    private modalService: NgbModal,
    public categoriesService: CategoriesService,
    private categoryDataService: CategoryDataService,
    private bookDataService: BookDataService
  ) {}

  ngOnInit(): void {
    this.categories$ = this.categoriesService.categories$;
    this.total$ = this.categoriesService.total$;
    merge(this.categoryDataService.refresh$, this.bookDataService.refresh$).subscribe(
      (v) => {
        this.categoriesService.refresh();
      }
    );
  }

  onSort({ column, direction }: SortEvent) {
    this.column = column;
    this.direction = direction;
    this.categoriesService.sortColumn = column;
    this.categoriesService.sortDirection = direction;
  }

  addCategory(): void {
    const modalRef = this.modalService.open(AddCategoryDialogComponent);
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }

  deleteCategory(category: Category): void {
    const modalRef = this.modalService.open(DeleteCategoryDialogComponent);
    modalRef.componentInstance.category = category;
    modalRef.result.then(
      (val) => {},
      (error) => {}
    );
  }
}
