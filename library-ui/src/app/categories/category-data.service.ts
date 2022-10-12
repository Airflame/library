import { HttpClient, HttpParams } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject } from 'rxjs';
import { Category } from '../model/category';
import { CategoryEntity } from '../model/category-entity';
import Application from '../../../application.json';

@Injectable({
  providedIn: 'root',
})
export class CategoryDataService {
  public refresh$: Subject<number> = new Subject();
  private apiPort: number;
  private categoriesUrl: string;
  private getCategoriesUrl: string;

  constructor(private http: HttpClient, private toastrService: ToastrService) {
    this.apiPort = Application.apiPort;
    this.categoriesUrl = `http://localhost:${this.apiPort}/api/categories`;
    this.getCategoriesUrl = `http://localhost:${this.apiPort}/api/categories/count`;
  }

  public getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.getCategoriesUrl);
  }

  public createCategory(category: CategoryEntity): void {
    this.http
      .post(this.categoriesUrl, category)
      .subscribe(
        (response) => {
          this.toastrService.success("Category successfully added");
          this.refresh$.next();
        },
        (error) => {
          this.toastrService.error(error.error.message, "Error " + error.status.toString());
        }
      );
  }

  public deleteCategory(id: number): void {
    this.http.delete(this.categoriesUrl + `/${id}`).subscribe(
      (response) => {
        this.toastrService.info("Category deleted");
        this.refresh$.next();
      },
      (error) => {
        this.toastrService.error(error.error.message, "Error " + error.status.toString());
      }
    );
  }
}
