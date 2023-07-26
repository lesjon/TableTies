import Person from "./Person";

export default interface Relation {
  id: number;
  person1: Person;
  person2: Person;
  relationStrength: number;
  event: Event;
}
