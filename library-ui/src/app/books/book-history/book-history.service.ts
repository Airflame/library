import { Injectable, PipeTransform } from '@angular/core';

import { BehaviorSubject, Observable, of, Subject } from 'rxjs';

import { DecimalPipe } from '@angular/common';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import { SortColumn, SortDirection } from './sortable.directive';
import { Lending } from 'src/app/model/lending';
import { BookDataService } from '../book-data.service';

interface SearchResult {
  lendings: Lending[];
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

function sort(lendings: Lending[], column: SortColumn, direction: string): Lending[] {
  if (direction === '' || column === '') {
    return [...lendings].sort((a, b) => {
      const res = compare(a["dateLent"], b["dateLent"]);
      if (!a["dateReturned"])
        return -1;
      if (!b["dateReturned"])
        return 1;
      return -res;
    });
  } else {
    return [...lendings].sort((a, b) => {
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(lending: Lending, term: string, pipe: PipeTransform) {
  return lending.id.toString().includes(term.toLowerCase())
    || lending.firstName.toString().toLowerCase().includes(term.toLowerCase())
    || lending.lastName.toString().toLowerCase().includes(term.toLowerCase())
    || lending.dateLent.toString().toLowerCase().includes(term.toLowerCase())
    || lending.dateReturned.toString().toLowerCase().includes(term.toLowerCase())
    || lending.comment.toString().toLowerCase().includes(term.toLowerCase());
}

@Injectable({
  providedIn: 'root',
})
export class BookHistoryService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _lendings$ = new BehaviorSubject<Lending[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private lendings: Lending[];

  private _state: State = {
    page: 1,
    pageSize: 5,
    searchTerm: '',
    sortColumn: '',
    sortDirection: ''
  };

  constructor(private pipe: DecimalPipe, private bookDataService: BookDataService) {}

  public refresh(id: number): void {
    this.bookDataService.getBookHistory(id).subscribe(result => {
      this.lendings = result.lendings;
      this._search$.pipe(
        tap(() => this._loading$.next(true)),
        switchMap(() => this._search()),
        tap(() => this._loading$.next(false))
      ).subscribe(result => {
        this._lendings$.next(result.lendings);
        this._total$.next(result.total);
      });
      this._search$.next();
    });
  }


  get lendings$() { return this._lendings$.asObservable(); }
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
    let lendings = sort(this.lendings, sortColumn, sortDirection);

    // 2. filter
    lendings = lendings.filter(lendings => matches(lendings, searchTerm, this.pipe));
    const total = lendings.length;

    // 3. paginate
    lendings = lendings.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({lendings, total});
  }
}
