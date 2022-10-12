import { Injectable, PipeTransform } from '@angular/core';

import { BehaviorSubject, Observable, of, Subject } from 'rxjs';

import { AvailableBook } from '../../model/available-book';
import { BookDataService } from '../book-data.service';
import { DecimalPipe } from '@angular/common';
import { debounceTime, switchMap, tap } from 'rxjs/operators';
import { SortColumn, SortDirection } from './sortable.directive';

interface SearchResult {
  books: AvailableBook[];
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

function sort(books: AvailableBook[], column: SortColumn, direction: string): AvailableBook[] {
  if (direction === '' || column === '') {
    return books;
  } else {
    return [...books].sort((a, b) => {
      const res = compare(a[column], b[column]);
      return direction === 'asc' ? res : -res;
    });
  }
}

function matches(book: AvailableBook, term: string, pipe: PipeTransform) {
  return book.id.toString().includes(term.toLowerCase())
    || book.author.toString().toLowerCase().includes(term.toLowerCase())
    || book.title.toString().toLowerCase().includes(term.toLowerCase())
    || book.category.toString().toLowerCase().includes(term.toLowerCase());
}

@Injectable({
  providedIn: 'root',
})
export class AvailableBooksService {
  private _loading$ = new BehaviorSubject<boolean>(true);
  private _search$ = new Subject<void>();
  private _books$ = new BehaviorSubject<AvailableBook[]>([]);
  private _total$ = new BehaviorSubject<number>(0);
  private books: AvailableBook[];

  private _state: State = {
    page: 1,
    pageSize: 5,
    searchTerm: '',
    sortColumn: '',
    sortDirection: ''
  };

  constructor(private pipe: DecimalPipe, private bookDataService: BookDataService) {
    this.refresh();
  }

  public refresh(): void {
    this.bookDataService.getAvailableBooks().subscribe(result => {
      this.books = result;
      this._search$.pipe(
        tap(() => this._loading$.next(true)),
        switchMap(() => this._search()),
        tap(() => this._loading$.next(false))
      ).subscribe(result => {
        this._books$.next(result.books);
        this._total$.next(result.total);
      });
      this._search$.next();
    });
  }


  get books$() { return this._books$.asObservable(); }
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
    let books = sort(this.books, sortColumn, sortDirection);

    // 2. filter
    books = books.filter(books => matches(books, searchTerm, this.pipe));
    const total = books.length;

    // 3. paginate
    books = books.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
    return of({books, total});
  }
}
