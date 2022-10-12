import { TestBed } from '@angular/core/testing';

import { BookHistoryService } from './book-history.service';

describe('BookHistoryService', () => {
  let service: BookHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookHistoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
