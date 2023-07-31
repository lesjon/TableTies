import {AfterViewChecked, Component, ElementRef, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../service/event.service';
import Event from '../domain/Event';
import {PeopleService} from "../service/people.service";
import Person from "../domain/Person";
import Relation from "../domain/Relation";
import {RelationService} from "../service/relation.service";

declare var LeaderLine: any;

enum LinesState {
  INIT,
  CLEANING,
  DRAWING,
  DONE
}

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.css']
})
export class EventComponent implements AfterViewChecked {
  private id: number | null = null;
  event: Event | null = null;
  people: Person[] = [];
  otherPeople: Person[] = [];
  selectedPerson: Person | null = null;
  relations: Relation[] = [];
  lines: typeof LeaderLine[] = [];
  updateLines: LinesState = LinesState.INIT;

  selectedPersonDiv: EventTarget | null = null;
  @ViewChild('otherPeopleDiv') otherPeopleDiv: ElementRef<HTMLDivElement> | undefined;

  constructor(private route: ActivatedRoute,
              private eventService: EventService,
              private peopleService: PeopleService,
              private relationService: RelationService) {
    this.route.params.subscribe((params: { [x: string]: number | null; }) => {
      this.id = params['id'];
      this.eventService.getEvent(this.id!).subscribe(event => {
        this.event = event;
      });
      this.peopleService.getPeople(this.id!).subscribe(people => {
        this.people = people;
        this.otherPeople = [];
      });
      this.relationService.getRelations(this.id!).subscribe(relations => {
        this.relations = relations;
      });
    });
  }

  addPerson(name: HTMLInputElement) {
    this.peopleService.createPerson(this.id!, name.value).subscribe(person => {
      this.people.push(person);
      name.value = '';
    });
  }

  selectPerson(person: Person, event: MouseEvent) {
    this.selectedPerson = person;
    this.selectedPersonDiv = event.target;
    this.otherPeople = this.people.filter(p => p.id !== person.id);
    this.updateLines = LinesState.CLEANING;
  }

  drawLines() {
    if (this.updateLines == LinesState.CLEANING) {
      this.lines.forEach(line => line.remove());
      this.lines = [];
      this.updateLines = LinesState.DRAWING;
      setTimeout(() => this.drawLines(), 0);
    } else if (this.updateLines == LinesState.DRAWING) {
      const otherPeopleDivs = this.otherPeopleDiv?.nativeElement.children;
      if (!otherPeopleDivs) {
        console.warn('no other people divs');
        return;
      }
      if(this.selectedPersonDiv === null){
        console.warn('no selected person div');
        return;
      }
      if(this.selectedPerson === null){
        console.warn('no selected person');
        return;
      }
      for (let i = 0; i < otherPeopleDivs.length; i++) {
        const otherPersonDiv = otherPeopleDivs[i];
        console.debug('drawing line from', this.selectedPersonDiv, 'to', otherPersonDiv);
        const otherPersonId = this.getPersonIdFromHtmlId(otherPersonDiv.id);
        if(!otherPersonId){
          console.warn('Could not parse other person id from html id:', otherPersonDiv.id);
          continue;
        }
        const otherPerson = this.people.find(p => p.id === parseInt(otherPersonId));
        if(!otherPerson){
          console.warn('no other person found with id:', otherPersonId);
          continue;
        }
        const relation = this.getRelationFor(this.selectedPerson, otherPerson);
        if(!relation){
          console.debug('no relation found', this.selectedPerson, otherPerson);
          continue;
        }
        const line = new LeaderLine(
          this.selectedPersonDiv,
          otherPersonDiv,
          {
            size: Math.abs(relation.relationStrength),
            endPlugOutline: false,
            color: relation.relationStrength > 0 ? '#0F0' : '#F00',
          });
        this.lines.push(line);
      }
      this.updateLines = LinesState.DONE;
    }
  }

  ngAfterViewChecked() {
    this.drawLines();
  }

  getRelationFor(person1: Person, person2: Person) {
    return this.relations
      .find(r => (r.person1.id === person1.id && r.person2.id === person2.id) ||
        (r.person1.id === person2.id && r.person2.id === person1.id));
  }

  getPersonIdFromHtmlId(htmlId: string) {
    return htmlId.split('person-').pop();
  }

  addRelation(person: Person) {
    if(!this.selectedPerson){
      console.warn('no selected person to create relation with');
      return;
    }
    this.relationService.createRelation(this.id!, this.selectedPerson.id, person.id, 3)
      .subscribe(relation => { this.relations.push(relation); });
  }
}
