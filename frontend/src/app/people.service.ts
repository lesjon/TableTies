import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Person from "./domain/Person";

@Injectable({
  providedIn: 'root'
})
export class PeopleService {

  constructor(private http: HttpClient) { }

  getPeople() {
    return this.http.get<Person[]>('http://localhost:8080/api/person');
  }
}
