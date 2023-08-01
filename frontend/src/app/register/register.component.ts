import { Component } from '@angular/core';
import {UserForm} from "../form/UserForm";
import KeycloakUser from "../domain/KeycloakUser";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  model = new UserForm('', '');
  loggedInUser?: KeycloakUser = undefined;
  failureMessage?: string = undefined;
  onSubmit() {
    this.userService.login(this.model.username, this.model.password).subscribe({
      next: data => {
        this.router.navigate(['login']);
      },
      error: error => {
        console.log(error);
        this.failureMessage = "Could register user";
      }
    });
  }

  constructor(private userService: UserService, private router: Router) { }

  goToLogin() {
    this.router.navigate(['login']);
  }
}

