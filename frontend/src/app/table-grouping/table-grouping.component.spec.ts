import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TableGroupingComponent } from './table-grouping.component';

describe('TableGroupingComponent', () => {
  let component: TableGroupingComponent;
  let fixture: ComponentFixture<TableGroupingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TableGroupingComponent]
    });
    fixture = TestBed.createComponent(TableGroupingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
