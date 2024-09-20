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
    const email: string | null = localStorage.getItem("email");
    const password: string | null = localStorage.getItem("password");

    if (email && password){
      this.userService.getUser(email, password).subscribe({
        next: user => {
          console.log(`Profil - constructor - userFromBack: ${JSON.stringify(user)}`);
          this
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
