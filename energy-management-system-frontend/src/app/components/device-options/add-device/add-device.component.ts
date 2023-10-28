import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {DeviceService} from "../../../services/device-service";
import {Router} from "@angular/router";
import {AddEditDeviceModel} from "../../../models/add-edit-device.model";

@Component({
  selector: 'app-add-device',
  templateUrl: './add-device.component.html',
  styleUrls: ['./add-device.component.css']
})
export class AddDeviceComponent implements OnInit {

  deviceForm: FormGroup;

  descriptionModel: string = "";
  addressModel: string = "";
  consumptionModel: number = 0.0;

  isDescriptionError: boolean;
  isAddressError: boolean;
  isConsumptionError: boolean;


  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<AddDeviceComponent>,
    private deviceService: DeviceService,
    private router: Router
  ) {
    this.deviceForm = this.formBuilder.group({
      description: ['', Validators.required],
      address: ['', [Validators.required, Validators.email]],
      consumption: ['', Validators.required],
    });

    this.isDescriptionError = false;
    this.isAddressError = false;
    this.isConsumptionError = false;
  }

  ngOnInit() {
  }

  onSubmit() {

    this.isDescriptionError = false;
    this.isAddressError = false;
    this.isConsumptionError = false;

    const description = this.deviceForm.get('description')?.value;
    const address = this.deviceForm.get('address')?.value;
    const consumption = this.deviceForm.get('consumption')?.value;

    if (description.length == 0) {
      this.isDescriptionError = true;
    }

    if (address.length == 0) {
      this.isAddressError = true;
    }

    if (consumption.length == 0) {
      this.isConsumptionError = true;
    }

    console.log('Description:', description);
    console.log('Address:', address);
    console.log('Consumption:', consumption);

    let addDeviceModel: AddEditDeviceModel = {
      description: this.descriptionModel,
      address: this.addressModel,
      consumption: this.consumptionModel
    };

    if (!this.isDescriptionError && !this.isAddressError && !this.isConsumptionError) {
      this.deviceService.addDevice(addDeviceModel).subscribe(data => {

        if (data.status == 201) {
          alert("Device successfully added!");

          this.reloadCurrentRoute();
        }
      }, error => {
        if (error.status == 400) {
          alert("The device could not be added!");
        }
      });

      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.deviceForm.value);
    }
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

}
