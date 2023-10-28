import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {DeviceService} from "../../services/device-service";
import {UserService} from "../../services/user-service";
import {MappingModel} from "../../models/mapping.model";

@Component({
  selector: 'app-mapping-options',
  templateUrl: './mapping-options.component.html',
  styleUrls: ['./mapping-options.component.css']
})
export class MappingOptionsComponent implements OnInit {

  mappings!: MappingModel[];

  constructor(private router: Router, private deviceService: DeviceService, private userService: UserService) {
  }

  ngOnInit() {
    this.deviceService.getAllMappings().subscribe(data => {
      console.log(data.body);
      this.mappings = data.body;

      for (let i = 0; i < this.mappings.length; i++) {
        this.getNameAndEmailAddressForUser(this.mappings[i].userID, i);
        this.getDeviceDescriptionAndAddressForDevice(this.mappings[i].deviceID, i);
      }
    }, error => {
      console.log(error);
      alert("Error occurred when getting all mappings!");
    });
  }

  navigateToMapping(action: string, userId?: number) {
    if (action === 'add') {
      this.router.navigate(['/add-mapping']);
    }
  }

  deleteMapping(deviceId: number) {
    console.log(deviceId);
    if (confirm('Are you sure you want to delete this user?')) {
      this.deviceService.deleteMapping(deviceId).subscribe((data) =>{
        alert("Mapping successfully deleted!");

        this.reloadCurrentRoute();
      }, (error) => {
        console.log(error);
        console.log("ERROR STATUS:");
        console.log(error.status);
        alert("ERROR WHEN DELETING MAPPING!");
      });
      console.log("Delete button was pressed!");
    }
  }

  getNameAndEmailAddressForUser(userId: number, index: number) {
    this.userService.getUserById(userId).subscribe(data => {
      console.log(data.body);
      this.mappings[index].name = data.body.name;
      this.mappings[index].email = data.body.email;
    }, error => {
      console.log(error);
      alert("Error while getting user with ID: " + userId);
    });
  }

  getDeviceDescriptionAndAddressForDevice(deviceId: number, index: number) {
    this.deviceService.getDeviceById(deviceId).subscribe(data => {
      console.log(data.body);
      this.mappings[index].deviceDescription = data.body.description;
      this.mappings[index].deviceAddress = data.body.address;
    }, error => {
      console.log(error);
      alert("Error while getting device with ID: " + deviceId);
    });
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

}
