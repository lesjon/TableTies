import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../service/event.service';
import Event from '../domain/Event';
import {PeopleService} from "../service/people.service";
import Person from "../domain/Person";

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements OnInit {
  private id: number | null = null;
  event: Event | null = null;
  people: Person[] = [];

  constructor(private route: ActivatedRoute,
              private eventService: EventService,
              private peopleService: PeopleService) {
    this.route.params.subscribe((params: { [x: string]: number | null; }) => {
      this.id = params['id'];
      this.eventService.getEvent(this.id!).subscribe(event => {
        console.log(event);
        this.event = event;
      });
      this.peopleService.getPeople(this.id!).subscribe(people => {
        console.log(people);
        this.people = people;
      });

    });
  }

  ngOnInit(): void {
  }

  addPerson(name: HTMLInputElement) {
    this.peopleService.createPerson(this.id!, name.value).subscribe(person => {
      this.people.push(person);
      name.value = '';
    });
  }
}
