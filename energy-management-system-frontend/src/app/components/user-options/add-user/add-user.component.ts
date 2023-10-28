import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatDialogRef} from "@angular/material/dialog";
import {UserService} from "../../../services/user-service";
import {AddUserModel} from "../../../models/add-user.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.css']
})
export class AddUserComponent implements OnInit {

  userForm: FormGroup;

  nameModel: string = "";
  emailModel: string = "";
  passwordModel: string = "";
  roleModel: string = "";

  isNameError: boolean;
  isEmailError: boolean;
  isPasswordError: boolean;
  isRoleError: boolean;


  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<AddUserComponent>,
    private userService: UserService,
    private router: Router
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

    console.log('Name:', name);
    console.log('Email:', email);
    console.log('Password:', password);
    console.log('Role:', role);

    let addUserModel: AddUserModel = {
      name: this.nameModel,
      email: this.emailModel,
      password: this.passwordModel,
      role: this.roleModel
    };

    if (!this.isNameError && !this.isEmailError && !this.isPasswordError && !this.isRoleError) {
      this.userService.addUser(addUserModel).subscribe(data => {
        if (data.status == 201) {
          alert("User successfully added!");

          this.reloadCurrentRoute();
        }
      }, error => {
        if (error.status == 404) {
          alert("The user could not be added!");
        }
      });

      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.userForm.value);
    }
  }

  reloadCurrentRoute() {
    let currentUrl = this.router.url;
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

}
