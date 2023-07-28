import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import Relation from "../domain/Relation";

@Injectable({
  providedIn: 'root'
})
export class RelationService {

  constructor(private http: HttpClient) { }

  getRelations(eventId: number) {
    return this.http.get<Relation[]>(`http://localhost:8080/api/event/${eventId}/relation`, { withCredentials: true });
  }

  createRelation(eventId: number, person1: string, person2: string) {
    return this.http.post<Relation>(`http://localhost:8080/api/event/${eventId}/relation`, { eventId, person1, person2}, { withCredentials: true });
  }
}
