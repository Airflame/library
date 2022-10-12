import { HttpClient, HttpParams } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject } from 'rxjs';
import { LendingHistory } from '../model/lending-history';
import Application from '../../../application.json';

@Injectable({
  providedIn: 'root',
})
export class LendingDataService {
  public refresh$: Subject<number> = new Subject();
  private apiPort: number;
  private lendingsUrl: string;
  private lendingHistoryUrl: string;

  constructor(private http: HttpClient, private toastrService: ToastrService) {
    this.apiPort = Application.apiPort;
    this.lendingsUrl = `http://localhost:${this.apiPort}/api/lendings`;
    this.lendingHistoryUrl = `http://localhost:${this.apiPort}/api/lendings/history`;
  }

  public lendBook(
    id: number,
    firstName: string,
    lastName: string,
    dateLent: string,
    comment: string
  ): void {
    let params = new HttpParams();
    params = params.append('firstName', firstName);
    params = params.append('lastName', lastName);
    if (dateLent != null) params = params.append('dateLent', dateLent);
    if (comment != '') params = params.append('comment', comment);
    this.http
      .post(this.lendingsUrl + `/${id}?` + params.toString(), null)
      .subscribe(
        (response) => {
          this.toastrService.success("Book successfully lent");
          this.refresh$.next();
        },
        (error) => {
          this.toastrService.error(error.error.message, "Error " + error.status.toString());
        }
      );
  }

  public returnBook(id: number, dateReturned: string): void {
    let params = new HttpParams();
    if (dateReturned != null)
      params = params.append('dateReturned', dateReturned);
    this.http
      .put(this.lendingsUrl + `/${id}?` + params.toString(), null)
      .subscribe(
        (response) => {
          this.toastrService.success("Book successfully returned");
          this.refresh$.next();
        },
        (error) => {
          this.toastrService.error(error.error.message, "Error " + error.status.toString());
        }
      );
  }

  public getLendingHistory(firstName: string, lastName: string): Observable<LendingHistory> {
    let params = new HttpParams();
    params = params.append('firstName', firstName);
    params = params.append('lastName', lastName);
    return this.http.get<LendingHistory>(this.lendingHistoryUrl + "?" + params.toString());
  }

  public deletePastLending(id: number): void {
    this.http.delete(this.lendingsUrl  + `/${id}`).subscribe(
      (response) => {
        this.toastrService.info("Lending deleted");
        this.refresh$.next();
      },
      (error) => {
        this.toastrService.error(error.error.message, "Error " + error.status.toString());
      }
    );
  }
}
