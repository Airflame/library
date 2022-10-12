import { TestBed } from '@angular/core/testing';

import { BorrowersService } from './borrowers.service';

describe('BorrowersService', () => {
  let service: BorrowersService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BorrowersService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
