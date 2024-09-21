import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Recipe } from '../../../utils/types';

@Injectable({
  providedIn: 'root',
})
export class RecipeDetailsService {
  apiUrl = 'http://localhost:8080/recipe/';

  constructor(private http: HttpClient) {}

  getRecipeInfo(recipeId: string): Observable<Recipe> {
    const url = `${this.apiUrl}${recipeId}`;
    return this.http.get<Recipe>(url).pipe(
      catchError((error) => {
        alert(error.message);
        return of({} as Recipe);
      })
    );
  }

  getAverageGrade(recipeId: string): Observable<number> {
    const url = `${this.apiUrl}average/${recipeId}`;
    return this.http.get<number>(url).pipe(
      catchError((error) => {
        alert(error.message);
        return of(0);
      })
    );
  }
}
