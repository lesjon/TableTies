import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HousingDetailComponent } from './housing-detail.component';

describe('HousingDetailComponent', () => {
  let component: HousingDetailComponent;
  let fixture: ComponentFixture<HousingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HousingDetailComponent]
    });
    fixture = TestBed.createComponent(HousingDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
