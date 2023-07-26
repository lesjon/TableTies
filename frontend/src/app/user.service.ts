import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import KeycloakUser from "./domain/KeycloakUser";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  getUser() {
    return this.http.get<KeycloakUser>('http://localhost:8080/api/user', { withCredentials: true });
  }
}
