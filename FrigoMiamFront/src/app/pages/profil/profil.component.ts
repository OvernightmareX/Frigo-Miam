import { Component } from '@angular/core';
import {UserService} from "../../services/user.service";
import {User} from "../../utils/types";

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [],
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {

  user: User;

  constructor(private userService: UserService) {
    this.user = userService.getUser();
  }
//todo: ngoninit appeler une methode sur service user, getUser devient getuserdata
  updateBtn(){

  }
  deleteBtn(){

    this.userService.deleteUser();
    //this.user = {};
  }
}
