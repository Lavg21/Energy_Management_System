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
import {UserOptionsComponent} from './components/user-options/user-options.component';
import {AddUserComponent} from './components/user-options/add-user/add-user.component';
import {MatDialogModule} from "@angular/material/dialog";
import {UserDialogService} from "./services/user-dialog.service";
import { EditUserComponent } from './components/user-options/edit-user/edit-user.component';
import { DeviceOptionsComponent } from './components/device-options/device-options.component';
import { AddDeviceComponent } from './components/device-options/add-device/add-device.component';
import { EditDeviceComponent } from './components/device-options/edit-device/edit-device.component';
import { MappingOptionsComponent } from './components/mapping-options/mapping-options.component';
import { AddMappingComponent } from './components/mapping-options/add-mapping/add-mapping.component';
import { UserDevicesComponent } from './components/user-devices/user-devices.component';
import { ChatComponent } from './components/chat/chat.component';
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AdminMenuComponent,
    NavbarComponent,
    UserOptionsComponent,
    AddUserComponent,
    EditUserComponent,
    DeviceOptionsComponent,
    AddDeviceComponent,
    EditDeviceComponent,
    MappingOptionsComponent,
    AddMappingComponent,
    UserDevicesComponent,
    ChatComponent
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
  providers: [UserDialogService, {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}, JwtHelperService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
