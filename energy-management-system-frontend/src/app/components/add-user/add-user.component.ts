import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent {
  userForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<AddUserComponent>
  ) {
    this.userForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['', Validators.required]
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.userForm.valid) {
      const name = this.userForm.get('name')?.value;
      const email = this.userForm.get('email')?.value;
      const password = this.userForm.get('password')?.value;
      const role = this.userForm.get('role')?.value;

      if (email && password && name && role) {
        // Integrate with the backend
        console.log('Name:', name);
        console.log('Email:', email);
        console.log('Password:', password);
        console.log('Role:', role);
      }

      this.dialogRef.close(this.userForm.value); // Close the modal with user data
      console.log("User added");
    }
  }
}
