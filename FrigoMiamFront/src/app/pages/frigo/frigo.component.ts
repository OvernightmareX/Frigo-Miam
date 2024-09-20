import { Component } from '@angular/core';
import {IngredientSearchComponent} from "../../components/ingredient-search/ingredient-search.component";
import {IngredientTabFrigoComponent} from "../../components/ingredient-tab-frigo/ingredient-tab-frigo.component";
import {IngredientFrigo, Recipe, RecipeCard, RecipeDetails, RecipeMatched} from "../../utils/types";
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

  addIngredient(ingredientName: string): void {  // TODO a mettre dans util. Yaura de la merde aussi avec le type du paramètre d'entrée
    const addedIngredient: IngredientFrigo = {
      name: ingredientName,
      quantity: 1
    };
    this.allFrigoIngredients.push(addedIngredient);
    //this.allRecipeCardsData = this.recipeIngredientMatching();
  }

  deleteIngredientFromFrigo(ingredientToDelete: IngredientFrigo): void {
    this.allFrigoIngredients = this.allFrigoIngredients.filter((ingInFrigo) => ingInFrigo.name !== ingredientToDelete.name);
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

  convertToRecipeCards(recipeList: RecipeDetails[]): RecipeCard[] {
    return recipeList.map(recipeMatched => ({
      id: recipeMatched.id,
      title: recipeMatched.title,
      description: recipeMatched.description,
      enoughQuantity: true  // field not used in home but in frigo
    }));

  }
}
