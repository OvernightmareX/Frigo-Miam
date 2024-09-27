import { Component } from '@angular/core';
import {UserService} from "../../services/http/account/user.service";
import {User} from "../../utils/types";

@Component({
  selector: 'app-profil',
  standalone: true,
  imports: [],
  templateUrl: './profil.component.html',
  styleUrl: './profil.component.css'
})
export class ProfilComponent {

  user: User = {
    "lastname": "",
    "firstname": "",
    "birthdate": new Date(),
    "email": "",
    "diet": "",
    "allergies": [],
    "password": "",
  };

  constructor(private userService: UserService) {
    const token: string | null = localStorage.getItem("token");

    if (token){
      this.userService.getUser(token).subscribe({
        next: user => {
          this.user = user;
      },
        error: err => {
        console.error('Erreur lors de la récupération des ingrédients', err);
      }
      })
    }

  }
//todo: ngoninit appeler une methode sur service user, getUser devient getuserdata
  updateBtn(){

  }
  deleteBtn(){

    this.userService.deleteUser();
    //this.user = {};
  }
}
