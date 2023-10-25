import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-device',
  templateUrl: './add-device.component.html',
  styleUrls: ['./add-device.component.css']
})
export class AddDeviceComponent implements OnInit{

  deviceForm: FormGroup;

  isDescriptionError: boolean;
  isAddressError: boolean;
  isConsumptionError: boolean;


  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<AddDeviceComponent>
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
    // Integrate with the backend
    console.log('Description:', description);
    console.log('Address:', address);
    console.log('Consumption:', consumption);

    if (!this.isDescriptionError && !this.isAddressError && !this.isConsumptionError) {
      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.deviceForm.value);
    }
  }

}
