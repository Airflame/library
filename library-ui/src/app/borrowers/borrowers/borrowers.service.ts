import { Injectable, PipeTransform } from '@angular/core';

import { BehaviorSubject, Observable, of, Subject } from 'rxjs';

import { BorrowerDataService } from '../borrower-data.service';
import { DecimalPipe } from '@angular/common';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import { SortColumn, SortDirection } from './sortable.directive';
import { Borrower } from 'src/app/model/borrower';

interface SearchResult {
  borrowers: Borrower[];
  total: number;
}

interface State {
  page: number;
  pageSize: number;
  searchTerm: string;
  sortColumn: SortColumn;
  sortDirection: SortDirection;
}

const compare = (v1: string | number, v2: string | number) => v1 < v2 ? -1 : v1 > v2 ? 1 : 0;

function sort(borrowers: Borrower[], column: SortColumn, direction: string): Borrower[] {
  if (direction === '' || column === '') {
    return borrowers;
  } else {
    return [...borrowers].sort((a, b) => {
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(borrower: Borrower, term: string, pipe: PipeTransform) {
  return borrower.id.toString().includes(term.toLowerCase())
    || borrower.firstName.toString().toLowerCase().includes(term.toLowerCase())
    || borrower.lastName.toString().toLowerCase().includes(term.toLowerCase());
}

@Injectable({
  providedIn: 'root'
})
export class BorrowersService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _borrowers$ = new BehaviorSubject<Borrower[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private borrowers: Borrower[];

  private _state: State = {
    page: 1,
    pageSize: 5,
    searchTerm: '',
    sortColumn: '',
    sortDirection: ''
  };

  constructor(private pipe: DecimalPipe, private borrowerDataService: BorrowerDataService) {
    this.refresh();
  }

  public refresh(): void {
    this.borrowerDataService.getAllBorrowers().subscribe(result => {
      this.borrowers = result;
      this._search$.pipe(
        tap(() => this._loading$.next(true)),
        switchMap(() => this._search()),
        tap(() => this._loading$.next(false))
      ).subscribe(result => {
        this._borrowers$.next(result.borrowers);
        this._total$.next(result.total);
      });
      this._search$.next();
    });
  }


  get borrowers$() { return this._borrowers$.asObservable(); }
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
    let borrowers = sort(this.borrowers, sortColumn, sortDirection);

    // 2. filter
    borrowers = borrowers.filter(borrowers => matches(borrowers, searchTerm, this.pipe));
    const total = borrowers.length;

    // 3. paginate
    borrowers = borrowers.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({borrowers, total});
  }
}
