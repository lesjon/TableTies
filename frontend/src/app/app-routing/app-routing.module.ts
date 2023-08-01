import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from "../login/login.component";
import {NewEventComponent} from "../new-event/new-event.component";
import {HomeComponent} from "../home/home.component";
import {EventComponent} from "../event/event.component";
import {RegisterComponent} from "../register/register.component";
import {PeopleListComponent} from "../people-list/people-list.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'event', component: EventComponent},
  {path: 'event/create', component: NewEventComponent},
  {path: 'event/:id/people', component: PeopleListComponent},
  {path: 'event/:id', component: EventComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
