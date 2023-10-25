import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AdminMenuComponent} from "./components/admin-menu/admin-menu.component";
import {UserOptionsComponent} from "./components/user-options/user-options.component";
import {DeviceOptionsComponent} from "./components/device-options/device-options.component";
import {MappingOptionsComponent} from "./components/mapping-options/mapping-options.component";
import {AddMappingComponent} from "./components/mapping-options/add-mapping/add-mapping.component";

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
  },
  {
    path: 'device-options',
    component: DeviceOptionsComponent
  },
  {
    path: 'mapping-options',
    component: MappingOptionsComponent
  },
  {
    path: "add-mapping",
    component: AddMappingComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

export const RoutingComponents = [LoginComponent, UserOptionsComponent];
