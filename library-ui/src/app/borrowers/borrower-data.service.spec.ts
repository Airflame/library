import { TestBed } from '@angular/core/testing';

import { BorrowerDataService } from './borrower-data.service';

describe('BorrowerDataService', () => {
  let service: BorrowerDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BorrowerDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
