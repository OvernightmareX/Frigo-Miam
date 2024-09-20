import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IngredientBack, User} from "../../../utils/types";
import {catchError, Observable, of} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  apiUrl = 'http://localhost:8080/recipe/filter';

  constructor(private http: HttpClient) { }

  getRecipesBasedOnIngredients(ingredientList: IngredientBack[]): Observable<Object[]>{
    const body = {"ingredientList": ingredientList}
    console.log(`body: ${JSON.stringify(body)}`);
    return this.http.post<Object[]>(this.apiUrl, body).pipe(
      catchError(error => {
        alert(error.message);
        return of([]);
      })
    );
  }
}
