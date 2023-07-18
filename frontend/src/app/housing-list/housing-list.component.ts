import { Component, Input, Output, EventEmitter } from '@angular/core';
import { HousingLocation } from "../housing-location";



@Component({
  selector: 'app-housing-list',
  templateUrl: './housing-list.component.html',
  styleUrls: ['./housing-list.component.css']
})
export class HousingListComponent {

  @Input() locationList: HousingLocation[] = [];
  results: HousingLocation[] = [];

  @Output() selectedLocationEvent = new EventEmitter<HousingLocation>();


  constructor() { }



  searchHousingLocations(searchTerm: string) {
    console.log("Search Housing Locations", searchTerm);
    this.results = [];
    for (let location of this.locationList) {
      if (location.city.toLowerCase().includes(searchTerm.toLowerCase())) {
        this.results.push(location);
      }
    }
  }

  selectHousingLocation(location: HousingLocation) {
    console.log("Select Housing Location", location);
    this.selectedLocationEvent.emit(location);
  }
}
