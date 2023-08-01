import {Component} from '@angular/core';
import {MatDialog} from "@angular/material/dialog";
import {LogoutDialogComponent} from "./logout-dialog/logout-dialog.component";
import {UserService} from "./service/user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';

}

