import {Component, OnInit} from '@angular/core';
import {DeviceService} from "../../services/device-service";
import {UserService} from "../../services/user-service";
import {UserModel} from "../../models/user.model";
import {UserDevicesModel} from "../../models/user-devices.model";

@Component({
  selector: 'app-user-devices',
  templateUrl: './user-devices.component.html',
  styleUrls: ['./user-devices.component.css']
})
export class UserDevicesComponent implements OnInit{

  devices!: UserDevicesModel[];

  user!: UserModel;

  constructor(private deviceService: DeviceService, private userService: UserService) {
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

}
