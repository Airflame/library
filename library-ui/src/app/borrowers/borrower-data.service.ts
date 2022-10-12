import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subject } from 'rxjs';
import { Borrower } from '../model/borrower';
import Application from '../../../application.json';

@Injectable({
  providedIn: 'root'
})
export class BorrowerDataService {
  public refresh$: Subject<number> = new Subject();
  private apiPort: number;
  private borrowersUrl: string;

  constructor(private http: HttpClient, private toastrService: ToastrService) {
    this.apiPort = Application.apiPort;
    this.borrowersUrl = `http://localhost:${this.apiPort}/api/borrowers`;
  }

  public getAllBorrowers(): Observable<Borrower[]> {
    return this.http.get<Borrower[]>(this.borrowersUrl);
  }

  public createBorrower(borrower: Borrower): void {
    borrower.id = null;
    this.http
      .post(this.borrowersUrl, borrower)
      .subscribe(
        (response) => {
          this.toastrService.success("Borrower successfully added");
          this.refresh$.next();
        },
        (error) => {
          this.toastrService.error(error.error.message, "Error " + error.status.toString());
        }
      );
  }

  public deleteBorrower(id: number): void {
    this.http.delete(this.borrowersUrl + `/${id}`).subscribe(
      (response) => {
        this.toastrService.info("Borrower deleted");
        this.refresh$.next();
      },
      (error) => {
        this.toastrService.error(error.error.message, "Error " + error.status.toString());
      }
    );
  }
}
