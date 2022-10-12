import { TestBed } from '@angular/core/testing';

import { PastLendingsService } from './past-lendings.service';

describe('PastLendingsService', () => {
  let service: PastLendingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PastLendingsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
