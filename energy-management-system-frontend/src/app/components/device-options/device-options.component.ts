import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {DeviceDialogService} from "../../services/device-dialog.service";
import {DeviceModel} from "../../models/device.model";
import {DeviceService} from "../../services/device-service";

@Component({
  selector: 'app-device-options',
  templateUrl: './device-options.component.html',
  styleUrls: ['./device-options.component.css']
})
export class DeviceOptionsComponent {

  devices!: DeviceModel[];

  constructor(private router: Router, private deviceDialogService: DeviceDialogService, private deviceService: DeviceService) {
    this.deviceService.getAllDevices().subscribe((data) => {
      this.devices = [];

      for (let i = 0; i < data.body.length; i++) {
        this.devices.push(data.body[i]);
      }
    }, error => {
      alert(error);
    });
  }

  navigateToDeviceForm(action: string, deviceId?: number) {
    if (action === 'add') {

      this.deviceDialogService.openAddDevicePopup().afterClosed().subscribe(result => {
        if (result) {
          console.log('Device added:', result);
        }
      });
    } else if (action === 'edit' && deviceId) {

      this.deviceDialogService.openEditDevicePopup(deviceId).afterClosed().subscribe(result => {
        if (result) {
          console.log('Device edited:', result);
        }
      });
      console.log("Edit button was pressed!");
    }
  }

  deleteDevice(deviceId: number) {
    console.log(deviceId);
    if (confirm('Are you sure you want to delete this user?')) {
      this.deviceService.deletedevice(deviceId).subscribe((data) => {
        alert("Device successfully edited!");

        this.reloadCurrentRoute();
      }, (error) => {
        console.log(error);
        console.log("ERROR STATUS:");
        console.log(error.status);
        alert("ERROR WHEN DELETING DEVICE!");
      });

      console.log("Delete button was pressed!");
    }
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

}
