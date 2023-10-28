import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {DeviceService} from "../../../services/device-service";
import {Router} from "@angular/router";
import {AddEditDeviceModel} from "../../../models/add-edit-device.model";

@Component({
  selector: 'app-edit-device',
  templateUrl: './edit-device.component.html',
  styleUrls: ['./edit-device.component.css']
})
export class EditDeviceComponent implements OnInit {

  deviceForm: FormGroup;

  isDescriptionError: boolean;
  isAddressError: boolean;
  isConsumptionError: boolean;

  deviceId: number;

  descriptionModel: string = "";
  addressModel: string = "";
  consumptionModel: number = 0.0;


  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<EditDeviceComponent>,
    private deviceService: DeviceService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router
  ) {
    this.deviceService = deviceService;
    this.deviceId = data.deviceId;

    this.deviceForm = this.formBuilder.group({
      description: ['', Validators.required],
      address: ['', [Validators.required, Validators.email]],
      consumption: ['', Validators.required],
    });

    this.isDescriptionError = false;
    this.isAddressError = false;
    this.isConsumptionError = false;

    this.deviceService.getDeviceById(this.deviceId).subscribe(data => {
      if (data.status == 200) {
        console.log("GET SUCCESSFUL!");

        this.descriptionModel = data.body.description;
        this.addressModel = data.body.address;
        this.consumptionModel = data.body.consumption;
      }
    }, error => {
      alert("ERROR WHEN GETTING DEVICE BY ID!");
    });
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

    if (consumption.length < 0) {
      this.isConsumptionError = true;
    }
    // Integrate with the backend
    console.log('Description:', description);
    console.log('Address:', address);
    console.log('Consumption:', consumption);

    if (!this.isDescriptionError && !this.isAddressError && !this.isConsumptionError) {

      let deviceModel: AddEditDeviceModel = new AddEditDeviceModel(this.descriptionModel, this.addressModel, this.consumptionModel);

      this.deviceService.updateDevice(this.deviceId, deviceModel).subscribe((data) => {
        alert("Device successfully updated!");
      }, error => {
        alert("ERROR WHEN UPDATING DEVICE");
      });

      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.deviceForm.value);

      this.reloadCurrentRoute();
    }
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }
}
