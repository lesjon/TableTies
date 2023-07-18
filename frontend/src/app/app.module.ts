import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HousingListComponent } from './housing-list/housing-list.component';
import {NgOptimizedImage} from "@angular/common";
import { HousingDetailComponent } from './housing-detail/housing-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    HousingListComponent,
    HousingDetailComponent
  ],
  imports: [
    BrowserModule,
    NgOptimizedImage
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
