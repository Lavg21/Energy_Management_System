import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {UserDialogService} from "../../services/user-dialog.service";

@Component({
  selector: 'app-user-options',
  templateUrl: './user-options.component.html',
  styleUrls: ['./user-options.component.css']
})
export class UserOptionsComponent {

  users: any[] = [
    { id: 1, name: 'John Doe', email: 'john@example.com', role: "admin" },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com', role: "user" },
  ];

  // constructor(private router: Router, private userDialogService: UserDialogService) {}
  //
  // navigateToUserForm(action: string, userId?: number) {
  //   if (action === 'add') {
  //     // Open the user popup
  //     this.userDialogService.openUserPopup().afterClosed().subscribe(result => {
  //       // Handle the result if needed (e.g., result could contain user data)
  //       if (result) {
  //         console.log('User added:', result);
  //         // You can also save the new user data to your users array
  //       }
  //     });
  //   } else if (action === 'edit' && userId) {
  //     // Open the edit popup
  //     console.log("Edit button was pressed!");
  //   }
  // }

  constructor(private router: Router) {}

  navigateToUserForm(action: string, userId?: number) {
    if (action === 'add') {
      // Redirect to the user form for adding a new user
      this.router.navigate(['/admin/user-management/edit-user']);
      console.log("Add button was pressed!");
    } else if (action === 'edit' && userId) {
      // Redirect to the user form for editing an existing user
      this.router.navigate(['/admin/user-management/edit-user', userId]);
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
