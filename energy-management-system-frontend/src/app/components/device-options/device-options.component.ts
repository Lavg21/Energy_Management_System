import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {UserDialogService} from "../../services/user-dialog.service";

@Component({
  selector: 'app-device-options',
  templateUrl: './device-options.component.html',
  styleUrls: ['./device-options.component.css']
})
export class DeviceOptionsComponent {

  devices: any[] = [
    { id: 1, description: 'Device 1', address: 'Cluj-Napoca', consumption: "3" },
    { id: 2, description: 'Device 2', address: 'Suceava', consumption: "4" },
  ];

  constructor(private router: Router, private userDialogService: UserDialogService) {
    // this.devices = this.deviceService.getAllDevices();
  }

  navigateToDeviceForm(action: string, userId?: number) {
    if (action === 'add') {

      this.userDialogService.openAddUserPopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('Device added:', result);
        }
      });
    } else if (action === 'edit' && userId) {

      this.userDialogService.openEditUserPopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('Device edited:', result);
        }
      });
      console.log("Edit button was pressed!");
    }
  }

  deleteDevice(userId: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.devices = this.devices.filter(device => device.id !== userId);
      console.log("Delete button was pressed!");
    }
  }

}