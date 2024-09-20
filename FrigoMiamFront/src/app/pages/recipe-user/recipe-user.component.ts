import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-recipe-user',
  standalone: true,
  imports: [],
  templateUrl: './recipe-user.component.html',
  styleUrl: './recipe-user.component.css'
})
export class RecipeUserComponent {

  constructor(private router: Router) {
  }

  createRecipe(){
    this.router.navigate(['/recetteCreate']);
  }
  BtnDeleteRecipe(){

  }
  BtnUpdateRecipe(){

  }
}

