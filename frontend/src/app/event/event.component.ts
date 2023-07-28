import {AfterViewChecked, AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EventService} from '../service/event.service';
import Event from '../domain/Event';
import {PeopleService} from "../service/people.service";
import Person from "../domain/Person";
import Relation from "../domain/Relation";
import {RelationService} from "../service/relation.service";

declare var LeaderLine: any;


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
    updateLines: boolean = false;

    selectedPersonDiv: EventTarget | null = null;
    @ViewChild('otherPeopleDiv') otherPeopleDiv: ElementRef<HTMLDivElement> | undefined;

    constructor(private route: ActivatedRoute,
                private eventService: EventService,
                private peopleService: PeopleService,
                private relationService: RelationService) {
        this.route.params.subscribe((params: { [x: string]: number | null; }) => {
            this.id = params['id'];
            this.eventService.getEvent(this.id!).subscribe(event => {
                console.log(event);
                this.event = event;
            });
            this.peopleService.getPeople(this.id!).subscribe(people => {
                console.log(people);
                this.people = people;
                this.otherPeople = [];
            });
            this.relationService.getRelations(this.id!).subscribe(relations => {
                console.log(relations);
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
        this.updateLines = true;
    }

    drawLines() {
        this.lines.forEach(line => line.remove());
        this.lines = [];
        const otherPeopleDivs = this.otherPeopleDiv?.nativeElement.children;
        if (!otherPeopleDivs || this.selectedPersonDiv === null) {
            console.log('otherPeopleDivs or selectedPersonDiv is null');
            return;
        }
        for (let i = 0; i < otherPeopleDivs.length; i++) {
            const otherPersonDiv = otherPeopleDivs[i];
            const line = new LeaderLine(
                this.selectedPersonDiv,
                otherPersonDiv,
                {
                    endPlugOutline: false,
                    animOptions: {duration: 3000, timing: 'linear'}
                });
            this.lines.push(line);
        }
    }

    ngAfterViewChecked() {
        if (this.updateLines) {
            this.drawLines();
            this.updateLines = false;
        }
    }

    getRelationsFor(person: Person) {
        return this.relations.filter(r => r.person1.id === person.id || r.person2.id === person.id);
    }
}
