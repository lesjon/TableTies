import { TestBed } from '@angular/core/testing';

import { TableGroupService } from './table-group.service';

describe('GroupTableService', () => {
  let service: TableGroupService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TableGroupService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
