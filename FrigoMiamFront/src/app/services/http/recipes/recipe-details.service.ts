import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of } from 'rxjs';
import { Recipe, RecipeDetails } from '../../../utils/types';

@Injectable({
  providedIn: 'root',
})
export class RecipeDetailsService {
  apiUrl = 'http://localhost:8080/recipe/';
  recipeId: string = '';

  recipe: RecipeDetails = { id: '', title: '', description: '', instructions: '', preparation_time: 0, cooking_time: 0, calories: 0, typeRecipe: '', diet: '', validation: '', ingredients: [] };

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
     this.getRecipeInfo(this.recipeId).subscribe({
      next: (data) => {
        this.recipe = data;
      },
      error: (error) => {
        console.error(error);
      },
    });
  }

  getRecipeInfo(recipeId: string): Observable<RecipeDetails> {
    const url = `${this.apiUrl}${recipeId}`;
    return this.http.get<RecipeDetails>(url).pipe(
      catchError((error) => {
        alert(error.message);
        return of({} as RecipeDetails);
      })
    );
  }
}
