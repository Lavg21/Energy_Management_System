import {Component} from '@angular/core';
import {UserService} from "../../../services/user-service";
import {DeviceService} from "../../../services/device-service";
import {UserMappingModel} from "../../../models/user-mapping.model";
import {DeviceMappingModel} from "../../../models/device-mapping.model";
import {MappingModel} from "../../../models/mapping.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-mapping',
  templateUrl: './add-mapping.component.html',
  styleUrls: ['./add-mapping.component.css']
})
export class AddMappingComponent {

  users!: UserMappingModel[];
  devices!: DeviceMappingModel[];


  constructor(private userService: UserService, private deviceService: DeviceService, private router: Router) {
    this.userService.getAllUsers().subscribe((data) => {
      this.users = [];
      for (let i = 0; i < data.body.length; i++) {
        this.users.push(this.userBodyToModel(data.body[i]));
      }
    }, error => {
      alert(error);
    });

    this.deviceService.getAllUnmappedDevices().subscribe((data) => {
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
      let mappingModel: MappingModel = new MappingModel(selectedUsers[0].id, selectedDevices[0].id);

      console.log(mappingModel);
      this.deviceService.addMapping(mappingModel).subscribe(data => {
        if (data.status == 201) {
          alert("Mapping successfully added!");

          this.reloadCurrentRoute();
        }
      }, error => {
        console.log(error.body);
        if (error.status == 404) {
          alert("The mapping could not be added!");
        }
      });
    } else {
      alert("Select one user and one device to create a mapping.");
      console.log('Select one user and one device to create a mapping.');
    }
  }

  private userBodyToModel(body: any) {
    return new UserMappingModel(body.id, body.name, body.email, body.checked);
  }

  private deviceBodyToModel(body: any) {
    return new DeviceMappingModel(body.id, body.description, body.address, body.checked);
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

}
