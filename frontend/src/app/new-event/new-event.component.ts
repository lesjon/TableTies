import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EventForm} from "../form/EventForm";

@Component({
  selector: 'app-new-event',
  templateUrl: './new-event.component.html',
  styleUrls: ['./new-event.component.css']
})
export class NewEventComponent {
  model = new EventForm('', {username: ''});
  submitted = false;
  constructor(private http: HttpClient) { }
  onSubmit() {
    this.http.post('http://localhost:8080/api/event', this.model)
      .subscribe(
        (val) => {
          console.log("POST call successful value returned in body", val);
          this.model = val as EventForm;
          this.submitted = true;
        },
        response => {
          console.log("POST call in error", response);
        },
        () => {
          console.log("The POST observable is now completed.");
        });
  }

  clear() {
    this.model = new EventForm('', {username: ''});
  }
}
