import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { AvailableBook } from '../model/available-book';
import { LentBook } from '../model/lent-book';
import { BookHistory } from '../model/book-history';
import { ToastrService } from 'ngx-toastr';
import Application from '../../../application.json';

@Injectable({
  providedIn: 'root',
})
export class BookDataService {
  public refresh$: Subject<number> = new Subject();
  private apiPort: number;
  private availableBookUrl: string;
  private lentBookUrl: string;
  private bookHistoryUrl: string;
  private bookUrl: string;

  constructor(private http: HttpClient, private toastrService: ToastrService) {
    this.apiPort = Application.apiPort;
    this.availableBookUrl = `http://192.168.49.2:${this.apiPort}/api/books/available`;
    this.lentBookUrl = `http://192.168.49.2:${this.apiPort}/api/books/lent`;
    this.bookHistoryUrl = `http://192.168.49.2:${this.apiPort}/api/books/history`;
    this.bookUrl = `http://192.168.49.2:${this.apiPort}/api/books`;
  }

  public getAvailableBooks(): Observable<AvailableBook[]> {
    return this.http.get<AvailableBook[]>(this.availableBookUrl);
  }

  public getLentBooks(): Observable<LentBook[]> {
    return this.http.get<LentBook[]>(this.lentBookUrl);
  }

  public createBook(author: string, title: string, category: string): void {
    let params = new HttpParams();
    params = params.append('author', author);
    params = params.append('category', category);
    params = params.append('title', title);
    this.http.post(this.bookUrl + '?' + params.toString(), null).subscribe(
      (response) => {
        this.toastrService.success("Book successfully added");
        this.refresh$.next();
      },
      (error: HttpErrorResponse) => {
        this.toastrService.error(error.error.message, "Error " + error.status.toString());
      }
    );
  }

  public deleteBook(id: number): void {
    this.http.delete(this.bookUrl + `/${id}`).subscribe(
      (response) => {
        this.toastrService.info("Book deleted");
        this.refresh$.next();
      },
      (error) => {
        this.toastrService.error(error.error.message, "Error " + error.status.toString());
      }
    );
  }

  public getBookHistory(id: number): Observable<BookHistory> {
    return this.http.get<BookHistory>(this.bookHistoryUrl + `/${id}`);
  }
}
