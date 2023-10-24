import { Injectable } from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddUserComponent} from "../components/add-user/add-user.component";

@Injectable({
  providedIn: 'root'
})
export class UserDialogService {

  constructor(public dialog: MatDialog) {
  }

  openUserPopup() {
    return this.dialog.open(AddUserComponent, {
      width: '400px', // Set the width as per your design
    });
  }
}
