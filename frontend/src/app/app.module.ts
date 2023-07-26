import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {NgOptimizedImage} from "@angular/common";
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
