import { TestBed } from '@angular/core/testing';

import { CurrentBooksService } from './current-books.service';

describe('CurrentBooksService', () => {
  let service: CurrentBooksService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CurrentBooksService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
