import {Component, OnInit} from '@angular/core';
import { PeopleService } from '../service/people.service';
import Person from "../domain/Person";
import {ActivatedRoute} from "@angular/router";
@Component({
  selector: 'app-people-list',
  templateUrl: './people-list.component.html',
  styleUrls: ['./people-list.component.css']
})
export class PeopleListComponent implements OnInit{
  people: Person[] = [];
  private eventId: number | undefined;
  constructor(private peopleService: PeopleService,
              private route: ActivatedRoute) {}

  getPeople() {
    this.peopleService.getPeople(0)
      .subscribe((data: Person[])=>{
      console.log(data);
      this.people = data;
    })
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: { [x: string]: number | null; }) => {
      this.eventId = params['id'] ?? undefined;
      if(this.eventId !== undefined) {
        this.peopleService.getPeople(this.eventId).subscribe(people => {
          this.people = people;
        });
      }
    })
  }
}
