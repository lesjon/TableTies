import { Component } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { LoginForm } from "../form/LoginForm";
import KeycloakUser from "../domain/KeycloakUser";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  model = new LoginForm('', '');
  loggedInUser?: KeycloakUser = undefined;
  onSubmit() {
    console.log("loggin in with: ", this.model);
    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });    let body = new URLSearchParams();
    body.set('username', this.model.username);
    body.set('password', this.model.password);
    return this.http.post<any>('http://localhost:8080/login', body.toString(), { headers: headers, withCredentials: true })
      .subscribe(response => {console.log(response)});
  }
  constructor(private http: HttpClient) { }

  getLoggedInUser() {
    this.http.get('http://localhost:8080/api/user', { withCredentials: true })
      .subscribe(response => {this.loggedInUser = response as KeycloakUser});
  }
}
