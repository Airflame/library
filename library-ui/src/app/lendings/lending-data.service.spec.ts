import { TestBed } from '@angular/core/testing';

import { LendingDataService } from './lending-data.service';

describe('LendingDataService', () => {
  let service: LendingDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LendingDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
