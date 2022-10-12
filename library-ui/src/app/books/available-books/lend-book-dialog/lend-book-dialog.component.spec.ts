import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LendBookDialogComponent } from './lend-book-dialog.component';

describe('LendBookDialogComponent', () => {
  let component: LendBookDialogComponent;
  let fixture: ComponentFixture<LendBookDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LendBookDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LendBookDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
