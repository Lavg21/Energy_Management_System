import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {JWTPayload, UserService} from "../../services/user-service";
import {Router} from "@angular/router";
import jwtDecode from "jwt-decode";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  isEmailError: boolean;
  isPasswordError: boolean;

  responseError = null;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });

    this.isEmailError = false;
    this.isPasswordError = false;
  }

  ngOnInit() {
  }

  onSubmit() {
    this.isEmailError = false;
    this.isPasswordError = false;

    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;

    if (email.length == 0) {
      this.isEmailError = true;
    }

    if (password.length == 0) {
      this.isPasswordError = true;
    }

    if (!this.isEmailError && !this.isPasswordError) {
      console.log('Email:', email);
      console.log('Password:', password);

      this.userService.login(email, password).subscribe(() => {
          this.responseError = null;
          const token = localStorage.getItem("token");
          const payload = jwtDecode(token!) as JWTPayload;

          if (payload.scope === "ROLE_ADMIN") {
            this.router.navigate(["/admin-menu"]);
          } else {
            this.router.navigate(["/devices"]);
          }
        },
        error => {
          this.responseError = error.error;
          console.log(this.responseError);
        });
      this.loginForm.reset();
    }
  }

}
