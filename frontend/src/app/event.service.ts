import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Event from "./domain/Event";

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { }

  getEvents() {
    return this.http.get<Event[]>('http://localhost:8080/api/event');
  }
}
