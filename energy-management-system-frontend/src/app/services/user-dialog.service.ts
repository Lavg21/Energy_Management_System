import {Injectable} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddUserComponent} from "../components/user-options/add-user/add-user.component";
import {EditUserComponent} from "../components/user-options/edit-user/edit-user.component";
import {ChatComponent} from "../components/chat/chat.component";
import {UserModel} from "../models/user.model";

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

  openChatDialog(user: UserModel) {
    return this.dialog.open(ChatComponent, {
      width: '25%',
      maxHeight: '60vh',
      data: { user: user },
    });
  }
}
