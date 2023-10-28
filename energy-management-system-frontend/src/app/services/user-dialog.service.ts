import {Injectable} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddUserComponent} from "../components/user-options/add-user/add-user.component";
import {EditUserComponent} from "../components/user-options/edit-user/edit-user.component";

@Injectable({
  providedIn: 'root'
})
export class UserDialogService {

  constructor(public dialog: MatDialog) {
  }

  openAddUserPopup() {
    return this.dialog.open(AddUserComponent, {
      width: '25%',
      maxHeight: '60vh'
    });
  }

  openEditUserPopup(userId: number) {
    return this.dialog.open(EditUserComponent, {
      width: '25%',
      maxHeight: '60vh',
      data: {
        userId: userId
      }
    });
  }
}
