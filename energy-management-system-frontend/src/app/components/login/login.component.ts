import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  isEmailError: boolean;
  isPasswordError: boolean;

  constructor(private formBuilder: FormBuilder) {
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

      console.log("SUCCESSFULLY ADDED!");
      // redirect user to next page
    }
  }

}
