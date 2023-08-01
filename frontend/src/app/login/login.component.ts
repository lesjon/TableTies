import { Component } from '@angular/core';
import { UserForm } from "../form/UserForm";
import KeycloakUser from "../domain/KeycloakUser";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  model = new UserForm('', '');
  loggedInUser?: KeycloakUser = undefined;
  failureMessage?: string = undefined;
  onSubmit() {
    this.userService.login(this.model.username, this.model.password).subscribe({
        next: data => {
          this.getLoggedInUser();
        },
        error: error => {
          console.log(error);
          this.failureMessage = "Could not log in ";
        }
      });
  }

  constructor(private userService: UserService, private router: Router) { }

  getLoggedInUser() {
    this.userService.getUser()
      .subscribe({
        next: data => this.loggedInUser = data,
        error: error => this.loggedInUser = undefined,
      });
  }

  goToRegister() {
    this.router.navigate(['register']);
  }
}
