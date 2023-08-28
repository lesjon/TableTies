import Event from './Event';

export default interface TableGroup{
  id: number;
  target: number;
  capacity: number;
  event: Event;
  centroid: number[];
  assignedPeople: number[];
}
