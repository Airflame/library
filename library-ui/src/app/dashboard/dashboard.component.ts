import { Component, OnInit } from '@angular/core';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { Color, Label, SingleDataSet } from 'ng2-charts';
import { merge, Observable } from 'rxjs';
import { BookDataService } from '../books/book-data.service';
import { CategoryDataService } from '../categories/category-data.service';
import { LendingDataService } from '../lendings/lending-data.service';
import { Statistics } from '../model/statistics';
import { DashboardDataService } from './dashboard-data.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  statistics$: Observable<Statistics>;

  availabilityChartOptions: ChartOptions;
  availabilityChartLabels: Label[];
  availabilityChartData: SingleDataSet;
  availabilityChartType: ChartType;
  availabilityChartLegend: boolean;
  availabilityChartPlugins = [];

  categoryChartOptions: ChartOptions = {
    scales: {
      yAxes: [{
        ticks: {
          stepSize: 1
        }
      }]
    }
  };
  categoryChartLabels: Label[];
  categoryChartData: SingleDataSet;
  categoryChartType: ChartType;
  categoryChartLegend: boolean;
  categoryChartPlugins = [];

  timelineChartData: ChartDataSets[];
  timelineChartLabels: Label[];
  timelineChartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      yAxes: [
        {
          scaleLabel: {
            display: true,
            labelString: 'Books'
          },
          ticks: {
            // maxTicksLimit: 4,
            stepSize: 1,
            fontStyle: 'normal',
            fontSize: 13,
            beginAtZero: false,
            callback: (value) => {
              return `${value.toLocaleString()}`;
            },
          },
          gridLines: {
            drawOnChartArea: false,
            // color: '#676A6C',
          }
        }],
      xAxes: [{
        ticks: {
          stepSize: 4,
          fontStyle: 'normal',
          fontSize: 13,
          autoSkip: false,
          maxRotation: 90,
          minRotation: 90,
        },
        gridLines: {
          drawOnChartArea: false,
          // color: '#676A6C',
          lineWidth: 1.5
        }
      }]
    },
    hover: {
      mode: 'nearest',
      intersect: true
    },

  };
  timelineChartLegend = true;
  timelineChartType: ChartType = 'line';

  public statistics: Statistics;

  startDate: NgbDateStruct;
  endDate: NgbDateStruct;

  constructor(public dashboardDataService: DashboardDataService,
    private bookDataService: BookDataService,
    private categoryDataService: CategoryDataService,
    private lendingDataService: LendingDataService) { }

  ngOnInit(): void {
    this.availabilityChartOptions = this.createOptions();
    this.availabilityChartLabels = ['Books available', 'Books lent'];
    this.availabilityChartType = 'pie';
    this.availabilityChartLegend = true;
    this.categoryChartType = 'bar';
    this.categoryChartLegend = false;
    this.statistics$ = this.dashboardDataService.getStatistics();
    merge(
      this.bookDataService.refresh$,
      this.categoryDataService.refresh$,
      this.lendingDataService.refresh$
    ).subscribe((av) => {
      this.refreshData();
    });
    this.statistics$.subscribe(s => {
      this.statistics = s;
      this.availabilityChartData = [s.availableBooks, s.lentBooks];
      this.timelineChartData = [{ data: Object.values(s.lendingTimeline.lent), label: "Books lent" }, { data: Object.values(s.lendingTimeline.returned), label: "Books returned" }, { data: Object.values(s.lendingTimeline.totalLent), label: "Total books lent" }];
      this.timelineChartLabels = Object.keys(s.lendingTimeline.lent);
      this.categoryChartData = s.categories.map(category => category.bookCount);
      this.categoryChartLabels = s.categories.map(category => category.name);
    });
  }

  public generateReports(): void {
    this.dashboardDataService.generateReports().subscribe(res => {
      const fileURL = URL.createObjectURL(res);
      window.open(fileURL, '_blank');
    });
  }

  public dateChange(): void {
    let timelineStart: string = this.startDate.year + "/" + (this.startDate.month < 10 ? '0' + this.startDate.month : this.startDate.month);
    let timelineEnd = this.endDate.year + "/" + (this.endDate.month < 10 ? '0' + this.endDate.month : this.endDate.month);
    let start = Object.keys(this.statistics.lendingTimeline.lent).indexOf(timelineStart);
    let end = Object.keys(this.statistics.lendingTimeline.lent).indexOf(timelineEnd);
    console.log(start);
    this.timelineChartLabels = Object.keys(this.statistics.lendingTimeline.lent).slice(start, end);
    this.timelineChartData = [{ data: Object.values(this.statistics.lendingTimeline.lent).slice(start, end), label: "Books lent" }, 
    { data: Object.values(this.statistics.lendingTimeline.returned).slice(start, end), label: "Books returned" }, 
    { data: Object.values(this.statistics.lendingTimeline.totalLent).slice(start, end), label: "Total books lent" }];
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
    this.statistics$ = this.dashboardDataService.getStatistics();
  }

}
