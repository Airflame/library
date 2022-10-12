import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { Observable } from 'rxjs';
import { Lending } from 'src/app/model/lending';
import { SortDirection } from '../available-books/sortable.directive';
import { BookDataService } from '../book-data.service';
import { BookHistoryService } from './book-history.service';
import { SortEvent } from './sortable.directive';

@Component({
  selector: 'app-book-history',
  templateUrl: './book-history.component.html',
  styleUrls: ['./book-history.component.scss'],
})
export class BookHistoryComponent implements OnInit {
  id: number;
  author: string;
  title: string;
  column: string;
  direction: SortDirection;
  lendings$: Observable<Lending[]>;
  total$: Observable<number>;

  constructor(
    private route: ActivatedRoute,
    public bookHistoryService: BookHistoryService,
    private bookDataService: BookDataService
  ) {}

  ngOnInit(): void {
    this.lendings$ = this.bookHistoryService.lendings$;
    this.total$ = this.bookHistoryService.total$;
    this.route.paramMap.subscribe((params: ParamMap) => {
      this.id = +params.get('id');
      this.bookHistoryService.refresh(this.id);
      this.bookDataService.getBookHistory(this.id).subscribe(result => {
        this.author = result.author;
        this.title = result.title;
      })
    });
  }

  onSort({ column, direction }: SortEvent) {
    this.column = column;
    this.direction = direction;
    this.bookHistoryService.sortColumn = column;
    this.bookHistoryService.sortDirection = direction;
  }
}
