import { Component } from '@angular/core';
import {IngredientSearchComponent} from "../../components/ingredient-search/ingredient-search.component";
import {IngredientListComponent} from "../../components/ingredient-list/ingredient-list.component";
import {RecipeCardShortComponent} from "../../components/recipe-card-short/recipe-card-short.component";
import {Recipe, RecipeCard} from "../../utils/types";

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    IngredientSearchComponent,
    IngredientListComponent,
    RecipeCardShortComponent
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  allRecipeCardsData: RecipeCard[] = [];

  recipes: Recipe[] = [ // TODO: will be replaced by values in service or localStorage
    {
      "ingredients": ["a", "aa", "aaa"],
      "nom": "Recette 1",
      "description": "1ere recette..."
    },
    {
      "ingredients": ["a", "aaa"],
      "nom": "Recette 2",
      "description": "2eme recette..."
    },
    {
      "ingredients": ["a", "aa"],
      "nom": "Recette 3",
      "description": "3eme recette..."
    },
    {
      "ingredients": ["aa", "aaa"],
      "nom": "Recette 4",
      "description": "4eme recette..."
    },
    {
      "ingredients": ["aaa"],
      "nom": "Recette 5",
      "description": "15eme recette..."
    },
    {
      "ingredients": ["aa"],
      "nom": "Recette 6",
      "description": "6eme recette..."
    }
  ]

  addIngredient(addedIngredient: string): void {
    this.allUserIngredients.push(addedIngredient)
    this.allRecipeCardsData = this.recipeIngredientMatching();
  }

  allUserIngredients: string[] = [];

  recipeIngredientMatching(): RecipeCard[]{
    const recipesFiltered: Recipe[] = [];

    this.recipes.forEach((recep) => {
      if (this.allUserIngredients.every(r => recep.ingredients.includes(r))){
        recipesFiltered.push(recep);
      }
    })

    return this.convertToRecipeCards(recipesFiltered);
  }

  convertToRecipeCards(recipeList: Recipe[]): RecipeCard[] {
    return recipeList.map(recipe => ({
      nom: recipe.nom,
      description: recipe.description
    }));
  }
}
