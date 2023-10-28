import {Injectable} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {AddDeviceComponent} from "../components/device-options/add-device/add-device.component";
import {EditDeviceComponent} from "../components/device-options/edit-device/edit-device.component";

@Injectable({
  providedIn: 'root'
})
export class DeviceDialogService {

  constructor(public dialog: MatDialog) {
  }

  openAddDevicePopup() {
    return this.dialog.open(AddDeviceComponent, {
      width: '25%',
      maxHeight: '60vh'
    });
  }

  openEditDevicePopup(deviceId: number) {
    return this.dialog.open(EditDeviceComponent, {
      width: '25%',
      maxHeight: '60vh',
      data: {
        deviceId: deviceId
      }
    });
  }
}
