import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {NgOptimizedImage} from "@angular/common";
import {MaterialModule} from "./material/material.module";

import {AppRoutingModule} from "./app-routing/app-routing.module";
import {HttpClientModule} from '@angular/common/http';
import {PeopleListComponent} from './people-list/people-list.component';
import {ToolBarComponent} from './tool-bar/tool-bar.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NewEventComponent} from './new-event/new-event.component';
import {FormsModule} from "@angular/forms";
import {LoginComponent} from './login/login.component';
import { HomeComponent } from './home/home.component';
import { EventComponent } from './event/event.component';
import {MatCardModule} from "@angular/material/card";
import {MatInputModule} from "@angular/material/input";

@NgModule({
  declarations: [
    AppComponent,
    PeopleListComponent,
    ToolBarComponent,
    NewEventComponent,
    LoginComponent,
    HomeComponent,
    EventComponent
  ],
  imports: [
    MaterialModule,
    BrowserModule,
    NgOptimizedImage,
    HttpClientModule,
    BrowserAnimationsModule,
    FormsModule,
    AppRoutingModule,
    MatCardModule,
    MatInputModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
