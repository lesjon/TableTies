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
import {MatSelectModule} from "@angular/material/select";
import { GroupTableComponent } from './group-table/group-table.component';
import { LogoutDialogComponent } from './logout-dialog/logout-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { RegisterComponent } from './register/register.component';
import {MatListModule} from "@angular/material/list";
import {MatLineModule} from "@angular/material/core";

@NgModule({
  declarations: [
    AppComponent,
    PeopleListComponent,
    ToolBarComponent,
    NewEventComponent,
    LoginComponent,
    HomeComponent,
    EventComponent,
    GroupTableComponent,
    LogoutDialogComponent,
    RegisterComponent
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
    MatInputModule,
    MatSelectModule,
    MatDialogModule,
    MatListModule,
    MatLineModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
