import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LendingHistoryComponent } from './lending-history.component';

describe('LendingHistoryComponent', () => {
  let component: LendingHistoryComponent;
  let fixture: ComponentFixture<LendingHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LendingHistoryComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LendingHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
