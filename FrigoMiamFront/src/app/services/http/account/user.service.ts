import { Injectable } from '@angular/core';
import {IngredientBack, User} from "../../../utils/types";
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserService {
 private user?: User;
  apiUrl = 'http://localhost:8080/account';

  constructor(private http: HttpClient) {}

  createUser(userToCreate: User): Observable<User>{
    console.log(`userToCreate: ${JSON.stringify(userToCreate)}`);
    return this.http.post<User>(this.apiUrl, userToCreate).pipe(
      catchError(error => {
        alert(error.message);
        return of({} as User);
      })
    );
  }

  getUser(token: string) : Observable<User>{
    const body: Object = {
      "token": token,
    }

    return this.http.post<User>(this.apiUrl+"/profil", body).pipe(
      tap(res => {
        console.log(`getUser - userFromBack: ${JSON.stringify(res)}`);
        this.user = res
      }),
      catchError(error => {
        alert(error.message);
        return of({} as User);
      })
    );

  }

  deleteUser(){
    //todo delete;
  }
  //todo creation methode getuserdata cette methode cets cette requete http qui envoie en get pour recuperer utilisateur il va chercher dans le service pour recuperer le back

}
