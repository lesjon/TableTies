import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Event from "../domain/Event";
import {EventForm} from "../form/EventForm";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { }

  getEvents() {
    return this.http.get<Event[]>('http://localhost:8080/api/event', { withCredentials: true });
  }

  postEvent(event: EventForm) {
    return this.http.post<Event>('http://localhost:8080/api/event', event, { withCredentials: true });
  }

  getEvent(id: number) {
    return this.http.get<Event>(`http://localhost:8080/api/event/${id}`, { withCredentials: true });
  }
}
