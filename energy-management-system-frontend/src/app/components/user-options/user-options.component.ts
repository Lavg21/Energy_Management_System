import {Component, EventEmitter, Output} from '@angular/core';
import {Router} from "@angular/router";
import {UserDialogService} from "../../services/user-dialog.service";
import {UserModel} from "../../models/user.model";
import {UserService} from "../../services/user-service";

@Component({
  selector: 'app-user-options',
  templateUrl: './user-options.component.html',
  styleUrls: ['./user-options.component.css'],
})
export class UserOptionsComponent {
  users!: UserModel[];
  admin:string = "superadmin@gmail.com";
  chart: any = [];
  showAlert!: boolean;
  message!: string;
  username !: "test";
  chat!: boolean;

  currentUser!: UserModel;

  constructor(private router: Router, private userDialogService: UserDialogService, private userService: UserService) {
    this.userService.getUserByToken().subscribe(data => {
      this.currentUser = data.body;
    }, error => {
      console.log(error.status)
      alert(error);
    });

    this.userService.getAllUsers().subscribe((data) => {
      this.users = [];
      for (let i = 0; i < data.body.length; i++) {
        this.users.push(this.bodyToModel(data.body[i]));
      }
    }, error => {
      alert(error);
    });
  }

  navigateToUserForm(action: string, userId?: number) {
    if (action === 'add') {

      this.userDialogService.openAddUserPopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('User added:', result);
        }
      });
    } else if (action === 'edit' && userId) {

      this.userDialogService.openEditUserPopup(userId).afterClosed().subscribe(result => {
        if (result) {
          console.log('User edited:', result);
        }
      });
      console.log("Edit button was pressed!");
    }
  }

  deleteUser(userId: number) {
    console.log(userId);
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.deleteUser(userId).subscribe((data) => {
        alert("User successfully deleted!");
        this.reloadCurrentRoute();
      }, (error) => {
        console.log(error);
        console.log("ERROR STATUS:");
        console.log(error.status);
        alert("ERROR WHEN DELETING USER!");
      });
      console.log("Delete button was pressed!");
    }
  }

  openChat(recipient: UserModel) {
    let emitter = this.currentUser;

    console.log(emitter);
    console.log(recipient);
    // this.router.navigate(['/chat', userId]);
    this.userDialogService.openChatDialog(emitter, recipient);
    this.chat = true;
   // this.sendMessageToUser.emit(user);
  }

  closeUserChat(user: string): void {
    this.chat = false;
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

  private bodyToModel(body: any) {
    if (body.admin) {
      return new UserModel(body.id, body.name, body.email, body.password, "ROLE_ADMIN");
    } else {
      return new UserModel(body.id, body.name, body.email, body.password, "ROLE_CLIENT");
    }
  }

}
