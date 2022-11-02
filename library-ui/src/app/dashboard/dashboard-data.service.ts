import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { map } from 'rxjs/operators';
import Application from '../../../application.json';
import { Statistics } from '../model/statistics';

@Injectable({
  providedIn: 'root'
})
export class DashboardDataService {
  private apiPort: number;
  private getStatisticsUrl: string;
  private generateReportsUrl: string;

  constructor(private http: HttpClient) {
    this.apiPort = Application.apiPort;
    this.getStatisticsUrl = `http://localhost:${this.apiPort}/api/stats/books-availability`;
    this.generateReportsUrl = `http://localhost:${this.apiPort}/api/reports`;
  }

  public getStatistics(): Observable<Statistics> {
    return this.http.get<Statistics>(this.getStatisticsUrl);
  }

  public generateReports(): any {
    const options = { responseType: 'blob' as 'json' };
    return this.http.post<Blob>(this.generateReportsUrl, options).pipe(map(
      (res) => {
          return new Blob([res], { type: 'application/pdf' });
      }))
    };
}
