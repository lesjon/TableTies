import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HousingListComponent} from './housing-list/housing-list.component';
import {NgOptimizedImage} from "@angular/common";
import {HousingDetailComponent} from './housing-detail/housing-detail.component';

import { HttpClientModule } from '@angular/common/http';
import { PeopleListComponent } from './people-list/people-list.component';

@NgModule({
  declarations: [
    AppComponent,
    HousingListComponent,
    HousingDetailComponent,
    PeopleListComponent
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
