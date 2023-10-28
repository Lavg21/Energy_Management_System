import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {UserService} from "../../../services/user-service";
import {EditUserModel} from "../../../models/edit-user.model";
import {Router} from "@angular/router";

@Component({
  selector: 'app-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.css']
})
export class EditUserComponent {

  userForm: FormGroup;

  isNameError: boolean;
  isEmailError: boolean;
  isRoleError: boolean;

  userId: number;

  nameModel: string = "";
  emailModel: string = "";
  roleModel: string = "";

  constructor(
    private formBuilder: FormBuilder,
    private dialogRef: MatDialogRef<EditUserComponent>,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private router: Router
  ) {
    this.userService = userService;
    this.userId = data.userId;

    this.userForm = this.formBuilder.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      role: ['', Validators.required]
    });

    this.isNameError = false;
    this.isEmailError = false;
    this.isRoleError = false;

    this.userService.getUserById(this.userId).subscribe(data => {
      if (data.status == 200) {
        console.log("GET SUCCESSFUL!");

        this.nameModel = data.body.name;
        this.emailModel = data.body.email;

        if (data.body.admin) {
          this.roleModel = "ADMIN";
        } else {
          this.roleModel = "CLIENT";
        }
      }
    }, error => {
      alert("ERROR WHEN GETTING USER BY ID!");
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    this.isNameError = false;
    this.isEmailError = false;
    this.isRoleError = false;

    const name = this.userForm.get('name')?.value;
    const email = this.userForm.get('email')?.value;
    const role = this.userForm.get('role')?.value;

    if (name.length == 0) {
      this.isNameError = true;
    }

    if (email.length == 0) {
      this.isEmailError = true;
    }

    if (role.length == 0 || (role.toUpperCase() != "ADMIN" && role.toUpperCase() != "CLIENT")) {
      this.isRoleError = true;
    }

    // Integrate with the backend
    console.log('Name:', name);
    console.log('Email:', email);
    console.log('Role:', role);

    if (!this.isNameError && !this.isEmailError && !this.isRoleError) {
      let isAdmin: boolean = false;
      if (this.roleModel == "ADMIN") {
        isAdmin = true;
      }

      let userModel: EditUserModel = new EditUserModel(this.nameModel, this.emailModel, isAdmin);

      this.userService.updateUser(this.userId, userModel).subscribe((data) => {
        alert("User successfully updated!");
      }, error => {
        alert("ERROR WHEN UPDATING USER!");
      });

      console.log("SUCCESSFULLY ADDED!");
      this.dialogRef.close(this.userForm.value);

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
