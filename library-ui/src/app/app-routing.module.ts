import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AvailableBooksComponent } from './books/available-books/available-books.component';
import { BookHistoryComponent } from './books/book-history/book-history.component';
import { LentBooksComponent } from './books/lent-books/lent-books.component';
import { BorrowersComponent } from './borrowers/borrowers/borrowers.component';
import { CategoriesComponent } from './categories/categories/categories.component';
import { LendingHistoryComponent } from './lendings/lending-history/lending-history.component';

const routes: Routes = [
  { path: 'books/available', component: AvailableBooksComponent },
  { path: 'books/lent', component: LentBooksComponent },
  { path: 'books/:id', component: BookHistoryComponent },
  { path: 'categories', component: CategoriesComponent },
  { path: 'borrowers', component: BorrowersComponent },
  { path: 'lendings', component: LendingHistoryComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
