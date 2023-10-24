import { Component } from '@angular/core';
import {Router} from "@angular/router";

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
