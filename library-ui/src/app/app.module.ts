import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AvailableBooksComponent } from './books/available-books/available-books.component';
import { NgbdSortableHeader } from './books/available-books/sortable.directive';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DecimalPipe } from '@angular/common';
import { CategoriesComponent } from './categories/categories/categories.component';
import { AddBookDialogComponent } from './books/available-books/add-book-dialog/add-book-dialog.component';
import { DeleteBookDialogComponent } from './books/available-books/delete-book-dialog/delete-book-dialog.component';
import { AddCategoryDialogComponent } from './categories/categories/add-category-dialog/add-category-dialog.component';
import { DeleteCategoryDialogComponent } from './categories/categories/delete-category-dialog/delete-category-dialog.component';
import { LendBookDialogComponent } from './books/available-books/lend-book-dialog/lend-book-dialog.component';
import { LentBooksComponent } from './books/lent-books/lent-books.component';
import { ReturnBookDialogComponent } from './books/lent-books/return-book-dialog/return-book-dialog.component';
import { BookHistoryComponent } from './books/book-history/book-history.component';
import { LendingHistoryComponent } from './lendings/lending-history/lending-history.component';
import { DeletePastLendingDialogComponent } from './lendings/lending-history/delete-past-lending-dialog/delete-past-lending-dialog.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BorrowersComponent } from './borrowers/borrowers/borrowers.component';
import { AddBorrowerDialogComponent } from './borrowers/borrowers/add-borrower-dialog/add-borrower-dialog.component';
import { DeleteBorrowerDialogComponent } from './borrowers/borrowers/delete-borrower-dialog/delete-borrower-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    AvailableBooksComponent,
    NgbdSortableHeader,
    CategoriesComponent,
    AddBookDialogComponent,
    DeleteBookDialogComponent,
    AddCategoryDialogComponent,
    DeleteCategoryDialogComponent,
    LendBookDialogComponent,
    LentBooksComponent,
    ReturnBookDialogComponent,
    BookHistoryComponent,
    LendingHistoryComponent,
    DeletePastLendingDialogComponent,
    BorrowersComponent,
    AddBorrowerDialogComponent,
    DeleteBorrowerDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot()
  ],
  providers: [DecimalPipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
