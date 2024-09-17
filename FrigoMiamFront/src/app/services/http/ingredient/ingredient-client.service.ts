import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, Observable, of} from "rxjs";
import {IngredientBack, User} from "../../../utils/types";

@Injectable({
  providedIn: 'root'
})
export class IngredientClientService {
  apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getAllIngredients(): Observable<IngredientBack[]>{
    return this.http.get<IngredientBack[]>(this.apiUrl + "/ingredient").pipe(
      catchError(error => {
        alert(error.message);
        return of({} as IngredientBack[]);
      })
    );
  }

  getIngredient(id: string): Observable<IngredientBack>{
    return this.http.get<IngredientBack>(this.apiUrl + "/ingredient/" + id).pipe(
      catchError(error => {
        alert(error.message);
        return of({} as IngredientBack);
      })
    );
  }
}
