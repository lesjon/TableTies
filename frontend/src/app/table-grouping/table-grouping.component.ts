import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {EventService} from "../service/event.service";
import {PeopleService} from "../service/people.service";
import TableGroup from "../domain/TableGroup";
import Person from "../domain/Person";

interface TableGroupWithPeople {
  tableGroup: TableGroup;
  people: Set<Person>;
}
@Component({
  selector: 'app-table-grouping',
  templateUrl: './table-grouping.component.html',
  styleUrls: ['./table-grouping.component.css', '../app.component.css']
})
export class TableGroupingComponent implements OnInit {
  id: number | null = null;
  groups: TableGroupWithPeople[] = [];

  constructor(private route: ActivatedRoute,
              private eventService: EventService,
              private peopleService: PeopleService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe((params: { [x: string]: number | null; }) => {
      this.id = params['id'];
      if (this.id === null || this.id === undefined) {
        console.error("No event id provided on init of TableGroupingComponent");
        return;
      }
      this.getTableGroups();
    });
  }

  getTableGroups() {
    this.peopleService.getPeople(this.id!).subscribe(people => {
      for(let person of people) {
        if(!person.tableGroup){
          console.warn("Person has no table group", person);
          continue;
        }
        const group = this.groups.find(g => g.tableGroup.id === person.tableGroup?.id);
        if(group) {
          group.people.add(person);
        }else{
          this.groups.push({
            tableGroup: person.tableGroup,
            people: new Set([person])
          });
        }
      }
    });

  }

  recompute() {
    this.groups = [];
    this.eventService.recompute(this.id!).subscribe(groups => {
      this.getTableGroups();
    });
  }
}
