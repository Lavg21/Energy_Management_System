import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {DeviceDialogService} from "../../services/device-dialog.service";

@Component({
  selector: 'app-mapping-options',
  templateUrl: './mapping-options.component.html',
  styleUrls: ['./mapping-options.component.css']
})
export class MappingOptionsComponent {

  mappings: any[] = [
    {id: 1, user: 'User 1', device: "Device 1", address: 'Cluj-Napoca'},
    {id: 1, user: 'User 2', device: "Device 1", address: 'Suceava'},
    {id: 1, user: 'User 3', device: "Device 2", address: 'Constanta'}
  ]

  constructor(private router: Router) {
    // this.devices = this.deviceService.getAllDevices();
  }

  navigateToMapping(action: string, userId?: number) {
    if (action === 'add') {
      this.router.navigate(['/add-mapping']);
    }
  }

  deleteMapping(mappingId: number) {
    if (confirm('Are you sure you want to delete this user?')) {
      this.mappings = this.mappings.filter(mapping => mapping.id !== mappingId);
      console.log("Delete button was pressed!");
    }
  }

}
