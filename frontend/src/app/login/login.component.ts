import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService) { }

  ngOnInit() { }

  onClick() {
    console.log(this.loginForm)
    this.authService.login(this.loginForm.username, this.loginForm.password)
      .subscribe(
        response => {
          console.log(response.message);
          this.authService.storeToken(response.token);
        },
        error => {
          console.error(error);
        }
      );
  }



}
