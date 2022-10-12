import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteBorrowerDialogComponent } from './delete-borrower-dialog.component';

describe('DeleteBorrowerDialogComponent', () => {
  let component: DeleteBorrowerDialogComponent;
  let fixture: ComponentFixture<DeleteBorrowerDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeleteBorrowerDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeleteBorrowerDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
