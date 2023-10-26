import {Component} from '@angular/core';

@Component({
  selector: 'app-add-mapping',
  templateUrl: './add-mapping.component.html',
  styleUrls: ['./add-mapping.component.css']
})
export class AddMappingComponent {
  users = [
    {name: 'Lavinia', email: 'lavi@example.com', checked: false},
    {name: 'Lavi', email: 'laviniaa@example.com', checked: false},
  ];

  devices = [
    {description: 'Device 1', address: 'Cluj-Napoca', checked: false},
    {description: 'Device 2', address: 'Suceava', checked: false},
  ];

  addMapping() {
    const selectedUsers = this.users.filter(user => user.checked);
    const selectedDevices = this.devices.filter(device => device.checked);

    if (selectedUsers.length === 1 && selectedDevices.length === 1) {
      console.log('Mapping user to device:', selectedUsers[0], selectedDevices[0]);
    } else {
      console.log('Select one user and one device to create a mapping.');
    }
  }
}
