import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  registerForm = {
    username: '',
    email: '',
    password: ''
  };

  constructor(private authService: AuthService) { }

  ngOnInit() { }

  onClick() {
    this.authService.signup(this.registerForm.username, this.registerForm.email, this.registerForm.password)
    .subscribe(
      response => {
        console.log(response.message);
      },
      error => {
        console.error(error);
      }
    );
  }
}
