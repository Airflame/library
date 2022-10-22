import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import Application from '../../../application.json';
import { Statistics } from '../model/statistics';

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {
  private apiPort: number;
  private getStatisticsUrl: string;

  constructor(private http: HttpClient) {
    this.apiPort = Application.apiPort;
    this.getStatisticsUrl = `http://localhost:${this.apiPort}/api/stats/books-availability`;
  }

  public getStatistics(): Observable<Statistics> {
    return this.http.get<Statistics>(this.getStatisticsUrl);
  }
}
