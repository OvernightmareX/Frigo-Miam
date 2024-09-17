import { Injectable } from '@angular/core';
import {User} from "../utils/types";

@Injectable({
  providedIn: 'root'
})
export class UserService {
 private user: User;

  constructor() { }

  getUser(){
    return this.user;

  }
  setUser(user: User){
    this.user = user;
    console.log(user)  }

  deleteUser(){
    //todo delete;
  }
  //todo creation methode getuserdata cette methode cets cette requete http qui envoie en get pour recuperer utilisateur il va chercher dans le service pour recuperer le back

}
