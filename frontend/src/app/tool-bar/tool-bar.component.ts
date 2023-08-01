import {Component, OnDestroy, OnInit} from '@angular/core';
import {EventService} from "../service/event.service";
import Event from "../domain/Event";
import KeycloakUser from "../domain/KeycloakUser";
import {UserService} from "../service/user.service";
import {Router} from "@angular/router";
import {MatTabChangeEvent} from '@angular/material/tabs';
import {Subscription} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {LogoutDialogComponent} from "../logout-dialog/logout-dialog.component";


@Component({
  selector: 'app-tool-bar',
  templateUrl: './tool-bar.component.html',
  styleUrls: ['./tool-bar.component.css']
})
export class ToolBarComponent implements OnInit, OnDestroy {
  events: Event[] = [];
  user: KeycloakUser | null = null;
  private subscription: Subscription | null = null;

  constructor(private eventService: EventService,
              private userService: UserService,
              private router: Router,
              public dialog: MatDialog) {
  }

  logout() {
    if(!this.user) {
      this.router.navigate(['login']);
      return;
    }
    const dialogRef = this.dialog.open(LogoutDialogComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.userService.logout().subscribe(() => {
          this.userService.clear();
          this.router.navigate(['login']);
        });
      }
    });
  }

  ngOnInit(): void {
    this.userService.getUser();
    this.subscription = this.userService.user$.subscribe(user => {
      this.user = user;
      this.getEvents();
    });
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe();
  }

  getEvents() {
    this.eventService.getEvents().subscribe(events => this.events = events);
  }

  urlFromEvent(event: Event) {
    return `/event/${event.id}`;
  }

  onTabChange(event: MatTabChangeEvent) {
    if (event.index === this.events.length) {  // If last tab (new) is selected
      this.router.navigate(['event/create']);
    } else {
      const selectedEvent = this.events[event.index];
      const url = this.urlFromEvent(selectedEvent);
      this.router.navigate([url]);
    }
  }
}
