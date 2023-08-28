import Event from './Event';
import TableGroup from "./TableGroup";

export default interface Person {
  id: number;
  name: string;
  event: Event;
  tableGroup?: TableGroup;
}
