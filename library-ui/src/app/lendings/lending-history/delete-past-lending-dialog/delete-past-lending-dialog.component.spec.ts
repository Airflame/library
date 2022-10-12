import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeletePastLendingDialogComponent } from './delete-past-lending-dialog.component';

describe('DeletePastLendingDialogComponent', () => {
  let component: DeletePastLendingDialogComponent;
  let fixture: ComponentFixture<DeletePastLendingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeletePastLendingDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DeletePastLendingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
