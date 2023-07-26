import { Component } from '@angular/core';
import { PeopleService } from '../people.service';
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
    this.peopleService.getPeople()
      .subscribe((data: Person[])=>{
      console.log(data);
      this.people = data;
    })
  }
}
