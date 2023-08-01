import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
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

  logout() {
    return this.http.post<void>('http://localhost:8080/logout', {}, { withCredentials: true });
  }

  login(username: string, password: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/x-www-form-urlencoded' });
    const body = new URLSearchParams();
    body.set('username', username);
    body.set('password', password);
    return this.http.post<any>('http://localhost:8080/login', body.toString(), { headers: headers, withCredentials: true });
  }

  clear() {
    this.userSubject.next(null);
  }
}
