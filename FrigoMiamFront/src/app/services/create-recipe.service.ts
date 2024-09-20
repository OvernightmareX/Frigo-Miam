import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CreateRecipeService {

  private apiUrl:string = 'http://localhost:8080';

  constructor( private http: HttpClient) { }

  createRecipe(recipeData: any): Observable<any>{
    const url = `${this.apiUrl}/recipe`;
    return this.http.post(url, recipeData);//send req post
  }
}

