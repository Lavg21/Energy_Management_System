import {Component, OnInit} from '@angular/core';
import {DeviceService} from "../../services/device-service";
import {UserService} from "../../services/user-service";
import {UserModel} from "../../models/user.model";
import {UserDevicesModel} from "../../models/user-devices.model";
import {UserDialogService} from "../../services/user-dialog.service";

@Component({
  selector: 'app-user-devices',
  templateUrl: './user-devices.component.html',
  styleUrls: ['./user-devices.component.css']
})
export class UserDevicesComponent implements OnInit{

  devices!: UserDevicesModel[];
  user!: UserModel;
  chat!: boolean;
  admin: string = "adminLavinia@gmail.com";
  adminId: number = 36;
  chart: any = [];
  showAlert!: boolean;
  message!: string;

  constructor(private deviceService: DeviceService, private userService: UserService, private userDialogService: UserDialogService) {
    this.userService.getUserByToken().subscribe(data => {
      this.user = data.body;
      this.getDevicesForUser(this.user.id);
    }, error => {
      console.log(error.status)
      alert(error);
    });
  }

  ngOnInit() {

  }

  getDevicesForUser(userId: number) {
    this.deviceService.getDevicesForUser(userId).subscribe(data => {
      console.log(data.body);
      this.devices = data.body;
    }, error => {
      console.log(error.status)
      alert(error);
    });
  }

  openChat() {
    let emitter: UserModel = this.user;
    let recipient: UserModel = new UserModel(this.adminId, "admin", this.admin, "", "ROLE_ADMIN");

    console.log(emitter);
    console.log(recipient);

    this.userDialogService.openChatDialog(emitter, recipient);
    // this.userDialogService.openChatDialog({ recipient: this.admin, emitter: user.email });
  }
}
