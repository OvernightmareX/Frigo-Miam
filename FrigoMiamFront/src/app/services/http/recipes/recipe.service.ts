import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {IngredientBack, RecipeDetails, User} from "../../../utils/types";
import {catchError, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class RecipeService {
  apiUrl = 'http://localhost:8080/recipe/filter';
  recipes: RecipeDetails[] = [];

  constructor(private http: HttpClient) { }

  getRecipesBasedOnIngredients(ingredientList: IngredientBack[]): Observable<RecipeDetails[]>{
    const body = {"ingredientList": ingredientList}
    console.log(`body: ${JSON.stringify(body)}`);
    return this.http.post<RecipeDetails[]>(this.apiUrl, body).pipe(
      tap(res => {
        console.log(`recipes response = ${JSON.stringify(res)}`);
        this.recipes = res;
      }),
      catchError(error => {
        alert(error.message);
        return of([] as RecipeDetails[]);
      })
    );
  }
}
