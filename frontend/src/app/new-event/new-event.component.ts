import {Component} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {EventForm} from "../form/EventForm";
import {UserService} from "../service/user.service";
import {mergeMap} from "rxjs";
import {EventService} from "../service/event.service";

@Component({
  selector: 'app-new-event',
  templateUrl: './new-event.component.html',
  styleUrls: ['./new-event.component.css']
})
export class NewEventComponent {
  model = new EventForm('', '');
  submitted = false;
  constructor(private http: HttpClient,
              private userService: UserService,
              private eventService: EventService) { }
  onSubmit() {
    if(this.model.user == '') {
      this.userService.getUser().pipe(
        mergeMap(response => {
          this.model.user = response.username;
          return this.eventService.postEvent(this.model);
        })
      ).subscribe({
        next: data => console.log('Received: ', data),
        error: error => console.log('Error: ', error),
      });
    } else {
      this.eventService.postEvent(this.model)
        .subscribe({
          next: data => console.log('Received: ', data),
          error: error => console.log('Error: ', error),
        });
    }
  }

  clear() {
    this.model = new EventForm('', '');
  }
}
