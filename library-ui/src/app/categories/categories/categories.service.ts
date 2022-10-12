import { Injectable, PipeTransform } from '@angular/core';

import { BehaviorSubject, Observable, of, Subject } from 'rxjs';

import { CategoryDataService } from '../category-data.service';
import { DecimalPipe } from '@angular/common';
import { debounceTime, delay, switchMap, tap } from 'rxjs/operators';
import { SortColumn, SortDirection } from './sortable.directive';
import { Category } from 'src/app/model/category';

interface SearchResult {
  categories: Category[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTerm: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number | boolean, v2: string | number | boolean) => v1 < v2 ? -1 : v1 > v2 ? 1 : 0;

function sort(categories: Category[], column: SortColumn, direction: string): Category[] {
  if (direction === '' || column === '') {
    return categories;
  } else {
    return [...categories].sort((a, b) => {
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(category: Category, term: string, pipe: PipeTransform) {
  return category.id.toString().includes(term.toLowerCase())
    || category.name.toString().toLowerCase().includes(term.toLowerCase())
    || category.bookCount.toString().toLowerCase().includes(term.toLowerCase());
}

@Injectable({
  providedIn: 'root',
})
export class CategoriesService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _categories$ = new BehaviorSubject<Category[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private categories: Category[];

  private _state: State = {
    page: 1,
    pageSize: 5,
    searchTerm: '',
    sortColumn: '',
    sortDirection: ''
  };

  constructor(private pipe: DecimalPipe, private categoryDataService: CategoryDataService) {
    this.refresh();
  }

  public refresh(): void {
    this.categoryDataService.getCategories().subscribe(result => {
      this.categories = result;
      this._search$.pipe(
        tap(() => this._loading$.next(true)),
        switchMap(() => this._search()),
        tap(() => this._loading$.next(false))
      ).subscribe(result => {
        this._categories$.next(result.categories);
        this._total$.next(result.total);
      });
      this._search$.next();
    });
  }


  get categories$() { return this._categories$.asObservable(); }
  get total$() { return this._total$.asObservable(); }
  get loading$() { return this._loading$.asObservable(); }
  get page() { return this._state.page; }
  get pageSize() { return this._state.pageSize; }
  get searchTerm() { return this._state.searchTerm; }

  set page(page: number) { this._set({page}); }
  set pageSize(pageSize: number) { this._set({pageSize}); }
  set searchTerm(searchTerm: string) { this._set({searchTerm}); }
  set sortColumn(sortColumn: SortColumn) { this._set({sortColumn}); }
  set sortDirection(sortDirection: SortDirection) { this._set({sortDirection}); }

  private _set(patch: Partial<State>) {
    Object.assign(this._state, patch);
    this._search$.next();
  }

  private _search(): Observable<SearchResult> {
    const {sortColumn, sortDirection, pageSize, page, searchTerm} = this._state;

    // 1. sort
    let categories = sort(this.categories, sortColumn, sortDirection);

    // 2. filter
    categories = categories.filter(categories => matches(categories, searchTerm, this.pipe));
    const total = categories.length;

    // 3. paginate
    categories = categories.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({categories, total});
  }
}
