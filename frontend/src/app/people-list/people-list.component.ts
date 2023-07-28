import { Component } from '@angular/core';
import { PeopleService } from '../service/people.service';
import Person from "../domain/Person";
@Component({
  selector: 'app-people-list',
  templateUrl: './people-list.component.html',
  styleUrls: ['./people-list.component.css']
})
export class PeopleListComponent {
  people: any[] = [];
  constructor(private peopleService: PeopleService) { }
  getPeople() {
    this.peopleService.getPeople(0)
      .subscribe((data: Person[])=>{
      console.log(data);
      this.people = data;
    })
  }
}
