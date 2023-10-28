import {Component} from '@angular/core';
import {UserService} from "../../../services/user-service";
import {DeviceService} from "../../../services/device-service";
import {UserMappingModel} from "../../../models/user-mapping.model";
import {DeviceMappingModel} from "../../../models/device-mapping.model";

@Component({
  selector: 'app-add-mapping',
  templateUrl: './add-mapping.component.html',
  styleUrls: ['./add-mapping.component.css']
})
export class AddMappingComponent {

  users!: UserMappingModel[];
  devices!: DeviceMappingModel[];


  constructor(private userService: UserService, private deviceService: DeviceService) {
    this.userService.getAllUsers().subscribe((data) => {
      this.users = [];
      for (let i = 0; i < data.body.length; i++) {
        this.users.push(this.userBodyToModel(data.body[i]));
      }
    }, error => {
      alert(error);
    });

    this.deviceService.getAllDevices().subscribe((data) => {
      this.devices = [];

      for (let i = 0; i < data.body.length; i++) {
        this.devices.push(this.deviceBodyToModel(data.body[i]));
      }
    }, error => {
      alert(error);
    });
  }

  addMapping() {
    const selectedUsers = this.users.filter(user => user.checked);
    const selectedDevices = this.devices.filter(device => device.checked);

    if (selectedUsers.length === 1 && selectedDevices.length === 1) {
      console.log('Mapping user to device:', selectedUsers[0], selectedDevices[0]);
    } else {
      console.log('Select one user and one device to create a mapping.');
    }
  }

  private userBodyToModel(body: any) {
    return new UserMappingModel(body.name, body.email, body.checked);
  }

  private deviceBodyToModel(body: any) {
    return new DeviceMappingModel(body.description, body.address, body.checked);
  }
}
