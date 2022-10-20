import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import Application from '../../../application.json';
import { StatsBooksAvailability } from '../model/stats-books-availability';

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {
  private apiPort: number;
  private getBooksAvailabilityUrl: string;

  constructor(private http: HttpClient) {
    this.apiPort = Application.apiPort;
    this.getBooksAvailabilityUrl = `http://localhost:${this.apiPort}/api/stats/books-availability`;
  }

  public getBooksAvailability(): Observable<StatsBooksAvailability> {
    return this.http.get<StatsBooksAvailability>(this.getBooksAvailabilityUrl);
  }
}
