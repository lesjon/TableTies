import { Component } from '@angular/core';
import {EventService} from "../service/event.service";
import Event from "../domain/Event";

@Component({
  selector: 'app-tool-bar',
  templateUrl: './tool-bar.component.html',
  styleUrls: ['./tool-bar.component.css']
})
export class ToolBarComponent {
  events: Event[] = [];
  constructor(private eventService: EventService) {
    this.getEvents();
  }
  toggleSidenav() {
    console.log("Toggle Sidenav");
  }

  getEvents() {
    this.eventService.getEvents()
      .subscribe(events => {
      console.log(events);
      this.events = events;
    });
  }
}
