import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from './components/login/login.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AdminMenuComponent} from './components/admin-menu/admin-menu.component';
import {NavbarComponent} from './components/navbar/navbar.component';
// <<<<<<< Updated upstream
// import {UserOptionsComponent} from "./components/user-options/user-options.component";
// import { AddUserComponent } from './components/add-user/add-user.component';
// import {MatDialogModule} from "@angular/material/dialog";
// =======
import {UserOptionsComponent} from './components/user-options/user-options.component';
import {AddUserComponent} from './components/user-options/add-user/add-user.component';
import {MatDialogModule} from "@angular/material/dialog";
import {UserDialogService} from "./services/user-dialog.service";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminMenuComponent,
    NavbarComponent,
    UserOptionsComponent,
    AddUserComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatDialogModule
  ],
  providers: [UserDialogService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
