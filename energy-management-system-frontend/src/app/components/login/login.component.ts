import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit{

  loginForm: FormGroup = new FormGroup({}); // Provide an empty FormGroup as an initializer

  constructor(private formBuilder: FormBuilder) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });
  }

  ngOnInit() {
  }

  onSubmit() {
    if (this.loginForm && this.loginForm.valid) {
      const email = this.loginForm.get('email')?.value;
      const password = this.loginForm.get('password')?.value;

      if (email && password) {
        // Integrate with the backend
        console.log('Email:', email);
        console.log('Password:', password);
      }
    }
  }
}
