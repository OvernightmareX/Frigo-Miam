import { Component } from '@angular/core';
import {IngredientSearchComponent} from "../../components/ingredient-search/ingredient-search.component";
import {IngredientTabFrigoComponent} from "../../components/ingredient-tab-frigo/ingredient-tab-frigo.component";
import {IngredientBack, IngredientFrigo, Recipe, RecipeCard, RecipeMatched} from "../../utils/types";
import {RecipeCardShortComponent} from "../../components/recipe-card-short/recipe-card-short.component";

@Component({
  selector: 'app-frigo',
  standalone: true,
  imports: [
    IngredientSearchComponent,
    IngredientTabFrigoComponent,
    RecipeCardShortComponent
  ],
  templateUrl: './frigo.component.html',
  styleUrl: './frigo.component.css'
})
export class FrigoComponent {

  allRecipeCardsData: RecipeCard[] = [];
  allFrigoIngredients: IngredientFrigo[] = [];
  recipes: Recipe[] = [ // TODO: will be replaced by values in service or localStorage
    // {
    //   "ingredients": ["a", "aa", "aaa"],
    //   "nom": "Recette 1",
    //   "description": "1ere recette..."
    // },
    // {
    //   "ingredients": ["a", "aaa"],
    //   "nom": "Recette 2",
    //   "description": "2eme recette..."
    // },
    // {
    //   "ingredients": ["a", "aa"],
    //   "nom": "Recette 3",
    //   "description": "3eme recette..."
    // },
    // {
    //   "ingredients": ["aa", "aaa"],
    //   "nom": "Recette 4",
    //   "description": "4eme recette..."
    // },
    // {
    //   "ingredients": ["aaa", "aaaa"],
    //   "nom": "Recette 5",
    //   "description": "15eme recette..."
    // },
    // {
    //   "ingredients": ["aa"],
    //   "nom": "Recette 6",
    //   "description": "6eme recette..."
    // }
  ]

  // appelé par la bar de recherche
  addIngredient(ingredientName: IngredientBack): void {
      // TODO a mettre dans util. Yaura de la merde aussi avec le type du paramètre d'entrée
    const addedIngredient: IngredientFrigo = {
      ingredient: ingredientName,
      quantity: 1
    };
    this.allFrigoIngredients.push(addedIngredient);
    //this.allRecipeCardsData = this.recipeIngredientMatching();
  }

  deleteIngredientFromFrigo(ingredientToDelete: IngredientFrigo): void {
    this.allFrigoIngredients = this.allFrigoIngredients.filter((ingInFrigo) => ingInFrigo.ingredient.name !== ingredientToDelete.ingredient.name);
    //this.allRecipeCardsData = this.recipeIngredientMatching();
  }

  changeQuantity(event: {quantity: number, ingredientChange: IngredientFrigo}): void {
    const { quantity, ingredientChange } = event;
    ingredientChange.quantity = quantity;
    // TODO, appeler fonction dessous quand quantité géré dans recette
    //this.recipeIngredientMatching();
  }

  addIngredientsToFrigo(): void {
    console.log(`addIngredientsToFrigo: ${JSON.stringify(this.allFrigoIngredients)}`);
  }

  convertToRecipeCards(recipeList: Recipe[]): RecipeCard[] {
    return recipeList.map(recipeMatched => ({
      recipe : recipeMatched,
      enoughQuantity: true  // field not used in home but in frigo
    }));

  }
}
