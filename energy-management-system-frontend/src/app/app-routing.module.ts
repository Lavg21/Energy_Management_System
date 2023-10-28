import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./components/login/login.component";
import {AdminMenuComponent} from "./components/admin-menu/admin-menu.component";
import {UserOptionsComponent} from "./components/user-options/user-options.component";
import {DeviceOptionsComponent} from "./components/device-options/device-options.component";
import {MappingOptionsComponent} from "./components/mapping-options/mapping-options.component";
import {AddMappingComponent} from "./components/mapping-options/add-mapping/add-mapping.component";
import {AuthGuard} from "./components/login/auth.guard";
import {UserDevicesComponent} from "./components/user-devices/user-devices.component";
import {AuthClientGuard} from "./components/login/auth-client.guard";

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
  { path: '',
    redirectTo: '/login',
    pathMatch: 'full' },
  {
    path: 'admin-menu',
    component: AdminMenuComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'user-options',
    component: UserOptionsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'device-options',
    component: DeviceOptionsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'mapping-options',
    component: MappingOptionsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "add-mapping",
    component: AddMappingComponent,
    canActivate: [AuthGuard]
  },
  {
    path: "devices",
    component: UserDevicesComponent,
    canActivate: [AuthClientGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}

export const RoutingComponents = [LoginComponent, UserOptionsComponent, MappingOptionsComponent, AddMappingComponent, UserDevicesComponent];
