import { Component, OnInit } from '@angular/core';
import { ChartOptions, ChartType } from 'chart.js';
import { Label, SingleDataSet } from 'ng2-charts';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  public SystemName: string = "MF1";
  public lineChartData: Array<number> = [1,8,49];

  public labelMFL: Array<any> = [
    { data: this.lineChartData,
      label: this.SystemName
    }
  ];

  public lineChartLabels: Array<any> = ["2018-01-29 10:00:00", "2018-01-29 10:27:00", "2018-01-29 10:28:00"];

  public lineChartOptions: any = {
    responsive: true,
    scales : {
      yAxes: [{
        ticks: {
          max : 60,
          min : 0,
        }
      }],
      xAxes: [{
  
 
        }],
    },
      plugins: {
      datalabels: {
        display: true,
        align: 'top',
        anchor: 'end',
        //color: "#2756B3",
        color: "#222",

        font: {
          family: 'FontAwesome',
          size: 14
        },
      
      },
      deferred: false

    },

  };

   _lineChartColors:Array<any> = [{
       backgroundColor: 'red',
        borderColor: 'red',
        pointBackgroundColor: 'red',
        pointBorderColor: 'red',
        pointHoverBackgroundColor: 'red',
        pointHoverBorderColor: 'red' 
      }];

  public ChartType = 'bar';

  public chartClicked(e: any): void {
    console.log(e);
  }
  public chartHovered(e: any): void {
    console.log(e);
  }

  pieChartOptions: ChartOptions;
  pieChartLabels: Label[];
  pieChartData: SingleDataSet;
  pieChartType: ChartType;
  pieChartLegend: boolean;
  pieChartPlugins = [];

  constructor() { }

  ngOnInit(): void {
    this.pieChartOptions = this.createOptions();
    this.pieChartLabels = ['January', 'February', 'March'];
    this.pieChartData = [50445, 33655, 15900];
    this.pieChartType = 'pie';
    this.pieChartLegend = true;
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

}
