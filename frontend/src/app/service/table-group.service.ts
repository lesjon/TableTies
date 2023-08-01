import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import TableGroup from "../domain/TableGroup";

@Injectable({
  providedIn: 'root'
})
export class TableGroupService {

  constructor(private http: HttpClient) { }

  createGroup(eventId: number, capacity: number, target?: number) {
    return this.http.post<TableGroup>(`http://localhost:8080/api/event/${eventId}/table`, {eventId, capacity, target}, { withCredentials: true });
  }

  getGroups(eventId: number) {
    return this.http.get<TableGroup[]>(`http://localhost:8080/api/event/${eventId}/table`, { withCredentials: true });
  }

  deleteGroup(eventId: number, tableGroupId: number) {
    return this.http.delete<void>(`http://localhost:8080/api/event/${eventId}/table/${tableGroupId}`, { withCredentials: true });
  }
}
