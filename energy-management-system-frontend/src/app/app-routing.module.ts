import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AdminMenuComponent} from "./components/admin-menu/admin-menu.component";
import {UserOptionsComponent} from "./components/user-options/user-options.component";

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'admin-menu',
    component: AdminMenuComponent
  },
  {
    path: 'user-options',
    component: UserOptionsComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [LoginComponent, UserOptionsComponent];
