import { TestBed } from '@angular/core/testing';

import { LentBooksService } from './lent-books.service';

describe('LentBooksService', () => {
  let service: LentBooksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LentBooksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
