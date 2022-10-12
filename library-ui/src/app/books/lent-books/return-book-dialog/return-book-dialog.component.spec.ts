import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReturnBookDialogComponent } from './return-book-dialog.component';

describe('ReturnBookDialogComponent', () => {
  let component: ReturnBookDialogComponent;
  let fixture: ComponentFixture<ReturnBookDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReturnBookDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReturnBookDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
