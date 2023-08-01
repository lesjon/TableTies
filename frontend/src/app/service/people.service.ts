import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import Person from "../domain/Person";

@Injectable({
  providedIn: 'root'
})
export class PeopleService {

  constructor(private http: HttpClient) { }

  getPeople(eventId: number) {
    return this.http.get<Person[]>(`http://localhost:8080/api/event/${eventId}/person`, { withCredentials: true });
  }

  createPerson(eventId: number, name: string) {
    return this.http.post<Person>(`http://localhost:8080/api/event/${eventId}/person`, { eventId, name }, { withCredentials: true });
  }

  deletePerson(eventId: number, person: Person) {
    return this.http.delete<Person>(`http://localhost:8080/api/event/${eventId}/person/${person.id}`, { withCredentials: true });
  }
}
