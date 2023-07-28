import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import KeycloakUser from "../domain/KeycloakUser";
import {BehaviorSubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private userSubject = new BehaviorSubject<KeycloakUser | null>(null);
  user$ = this.userSubject.asObservable();
  constructor(private http: HttpClient) { }

  getUser() {
    const user =  this.http.get<KeycloakUser>('http://localhost:8080/api/user', { withCredentials: true });
    user.subscribe(user => this.userSubject.next(user));
    return user;
  }
}
