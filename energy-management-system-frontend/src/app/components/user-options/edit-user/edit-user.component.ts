import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent {
  userForm: FormGroup;

  isNameError: boolean;

  isEmailError: boolean;

  isPasswordError: boolean;

  isRoleError: boolean;

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<EditUserComponent>
  ) {
    this.userForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['', Validators.required]
    });

    this.isNameError = false;
    this.isEmailError = false;
    this.isPasswordError = false;
    this.isRoleError = false;
  }

  ngOnInit() {
  }

  onSubmit() {
    this.isNameError = false;
    this.isEmailError = false;
    this.isPasswordError = false;
    this.isRoleError = false;

    const name = this.userForm.get('name')?.value;
    const email = this.userForm.get('email')?.value;
    const password = this.userForm.get('password')?.value;
    const role = this.userForm.get('role')?.value;

    if (name.length == 0) {
      this.isNameError = true;
    }

    if (email.length == 0) {
      this.isEmailError = true;
    }

    if (password.length == 0) {
      this.isPasswordError = true;
    }

    if (role.length == 0 || (role.toUpperCase() != "ADMIN" && role.toUpperCase() != "CLIENT")) {
      this.isRoleError = true;
    }

    // Integrate with the backend
    console.log('Name:', name);
    console.log('Email:', email);
    console.log('Password:', password);
    console.log('Role:', role);

    if (!this.isNameError && !this.isEmailError && !this.isPasswordError && !this.isRoleError) {
      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.userForm.value);
    }
  }
}
