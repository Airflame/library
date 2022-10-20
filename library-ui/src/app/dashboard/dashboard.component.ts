import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Label, SingleDataSet } from 'ng2-charts';
import { merge, Observable } from 'rxjs';
import { BookDataService } from '../books/book-data.service';
import { CategoryDataService } from '../categories/category-data.service';
import { LendingDataService } from '../lendings/lending-data.service';
import { StatsBooksAvailability } from '../model/stats-books-availability';
import { DashboardDataService } from './dashboard-data.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  availabilityStats$: Observable<StatsBooksAvailability>;

  availabilityChartOptions: ChartOptions;
  availabilityChartLabels: Label[];
  availabilityChartData: SingleDataSet;
  availabilityChartType: ChartType;
  availabilityChartLegend: boolean;
  availabilityChartPlugins = [];

  constructor(public dashboardDataService: DashboardDataService,
    private bookDataService: BookDataService,
    private categoryDataService: CategoryDataService,
    private lendingDataService: LendingDataService) { }

  ngOnInit(): void {
    this.availabilityChartOptions = this.createOptions();
    this.availabilityChartLabels = ['Books available', 'Books lent'];
    this.availabilityChartType = 'pie';
    this.availabilityChartLegend = true;
    this.availabilityStats$ = this.dashboardDataService.getBooksAvailability();
    merge(
      this.bookDataService.refresh$,
      this.categoryDataService.refresh$,
      this.lendingDataService.refresh$
    ).subscribe((av) => {
      this.refreshData();
    });
    this.availabilityStats$.subscribe(s => this.availabilityChartData = [s.available, s.lent])
  }

  private createOptions(): ChartOptions {
    return {
      responsive: true,
          maintainAspectRatio: true,
          plugins: {
              labels: {
                render: 'percentage',
                fontColor: ['green', 'white', 'red'],
                precision: 2
              }
          },
    };
  }

  private refreshData() {
    this.availabilityStats$ = this.dashboardDataService.getBooksAvailability();
  }

}
