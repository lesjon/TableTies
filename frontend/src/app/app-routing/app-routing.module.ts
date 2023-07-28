import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "../login/login.component";
import {NewEventComponent} from "../new-event/new-event.component";
import {HomeComponent} from "../home/home.component";
import {EventComponent} from "../event/event.component"; // CLI imports router

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'event/create', component: NewEventComponent },
  { path: 'event/:id', component: EventComponent},
  { path: 'event', redirectTo: 'event/create', pathMatch: 'full' },
];

// configures NgModule imports and exports
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
