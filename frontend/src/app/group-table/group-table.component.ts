import {Component, Input, OnInit} from '@angular/core';
import {TableGroupService} from "../service/table-group.service";
import TableGroup from "../domain/TableGroup";

@Component({
  selector: 'app-group-table',
  templateUrl: './group-table.component.html',
  styleUrls: ['./group-table.component.css']
})
export class GroupTableComponent implements OnInit{
  private _eventId: number | undefined;
  @Input() set eventId(value: number | undefined){
    this._eventId = value;
    this.ngOnInit();
  };
  groups: TableGroup[] = [];

  constructor(private tableGroupService: TableGroupService) {}

  ngOnInit(): void {
    if (this._eventId) {
      this.tableGroupService.getGroups(this._eventId!).subscribe(groups => {
        this.groups = groups;
      });
    }
  }

  addGroup(capacity: number | string, target?: number | string | null) {
    if (typeof capacity === 'string') {
      capacity = parseInt(capacity);
    }
    if (typeof target === 'string') {
      target = parseInt(target);
    }
    if (target === null || target === undefined || isNaN(target)) {
      target = capacity;
    }
    this.tableGroupService.createGroup(this._eventId!, capacity, target).subscribe(group => {
      this.groups.push(group);
    });
  }

  deleteGroup(id: number) {
    this.tableGroupService.deleteGroup(this._eventId!, id).subscribe(() => {
      this.groups = this.groups.filter(group => group.id !== id);
    });
  }
}
