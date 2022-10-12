import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBorrowerDialogComponent } from './add-borrower-dialog.component';

describe('AddBorrowerDialogComponent', () => {
  let component: AddBorrowerDialogComponent;
  let fixture: ComponentFixture<AddBorrowerDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddBorrowerDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddBorrowerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
