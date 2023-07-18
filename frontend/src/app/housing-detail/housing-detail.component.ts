import {Component, Input} from '@angular/core';
import {HousingLocation} from "../housing-location";

@Component({
  selector: 'app-housing-detail',
  templateUrl: './housing-detail.component.html',
  styleUrls: ['./housing-detail.component.css']
})
export class HousingDetailComponent {

  @Input() selectedLocation: HousingLocation | undefined;
}
