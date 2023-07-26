import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {HousingListComponent} from './housing-list/housing-list.component';
import {NgOptimizedImage} from "@angular/common";
import {HousingDetailComponent} from './housing-detail/housing-detail.component';
import {MaterialModule} from "./material/material.module";


import {HttpClientModule} from '@angular/common/http';
import {PeopleListComponent} from './people-list/people-list.component';
import {ToolBarComponent} from './tool-bar/tool-bar.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NewEventComponent} from './new-event/new-event.component';
import {FormsModule} from "@angular/forms";
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HousingListComponent,
    HousingDetailComponent,
    PeopleListComponent,
    ToolBarComponent,
    NewEventComponent,
    LoginComponent
  ],
  imports: [
    MaterialModule,
    BrowserModule,
    NgOptimizedImage,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
