import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {UserDialogService} from "../../services/user-dialog.service";

@Component({
  selector: 'app-user-options',
  templateUrl: './user-options.component.html',
  styleUrls: ['./user-options.component.css'],
})
export class UserOptionsComponent {

  users: any[] = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: "admin" },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: "user" },
  ];

  constructor(private router: Router, private userDialogService: UserDialogService) {
    // this.users = this.userService.getAllUsers();
  }

  navigateToUserForm(action: string, userId?: number) {
    if (action === 'add') {

      this.userDialogService.openAddUserPopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('User added:', result);
        }
      });
    } else if (action === 'edit' && userId) {

      this.userDialogService.openEditUserPopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('User edited:', result);
        }
      });
      console.log("Edit button was pressed!");
    }
  }

  deleteUser(userId: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.users = this.users.filter(user => user.id !== userId);
      console.log("Delete button was pressed!");
    }
  }

}
