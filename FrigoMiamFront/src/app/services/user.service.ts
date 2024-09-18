import { Injectable } from '@angular/core';
import {IngredientBack, User} from "../utils/types";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
 private user?: User;
  apiUrl = 'http://localhost:8080/account';

  createUserBody = {
    "firstname": "string",
    "lastname": "string",
    "email": "string@est.com",
    "password": "string",
    "birthdate": "2024-09-17",
    "allergies": "DAIRY",
    "diets": "VEGETARIAN"
  }


  constructor(private http: HttpClient) { }

  createUser(userToCreate: User): Observable<User>{
    return this.http.post<User>(this.apiUrl, userToCreate).pipe(
      catchError(error => {
        alert(error.message);
        return of({} as User);
      })
    );
  }

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
