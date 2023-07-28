import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { UserForm } from "../form/UserForm";
import KeycloakUser from "../domain/KeycloakUser";
import {UserService} from "../service/user.service";

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
    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });    let body = new URLSearchParams();
    body.set('username', this.model.username);
    body.set('password', this.model.password);
    return this.http.post<any>('http://localhost:8080/login', body.toString(), { headers: headers, withCredentials: true })
      .subscribe({
        next: data => {
          this.getLoggedInUser();
        },
        error: error => {
          console.log(error);
          this.failureMessage = "Could not log in ";
        }
      });
  }
  constructor(private http: HttpClient, private userService: UserService) { }

  getLoggedInUser() {
    this.userService.getUser()
      .subscribe({
        next: data => this.loggedInUser = data,
        error: error => this.loggedInUser = undefined,
      });
  }
}
