import { Component } from '@angular/core';
import { IngredientSearchComponent } from '../../components/ingredient-search/ingredient-search.component';
import { IngredientListComponent } from '../../components/ingredient-list-home/ingredient-list.component';
import { RecipeCardShortComponent } from '../../components/recipe-card-short/recipe-card-short.component';
import {
  IngredientBack,
  IngredientQuantity,
  Recipe,
  RecipeCard,
  RecipeMatched,
} from '../../utils/types';
import { IngredientClientService } from '../../services/http/ingredient/ingredient-client.service';
import { RecipeService } from '../../services/http/recipes/recipe.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    IngredientSearchComponent,
    IngredientListComponent,
    RecipeCardShortComponent,
    RouterLink,
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  constructor(
    private ingredientService: IngredientClientService,
    private recipeService: RecipeService
  ) {
    this.getAllIngredients();
  }

  allRecipeCardsData: RecipeCard[] = [];
  allUserIngredients: IngredientBack[] = [];

  ngOnInit(){
    console.log("Yolo")
    this.getAllRecipes(); 
  }

  getAllRecipes(): void {
    this.recipeService
    .getAllRecipes()
    .subscribe({
      next: (recipes) => {
        this.allRecipeCardsData = this.convertToRecipeCards(recipes);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des ingrédients', err);
      },
    });
  }

  addIngredient(addedIngredient: IngredientBack): void {
    // TODO a mettre dans util
    this.allUserIngredients.push(addedIngredient);
    console.log(
      `Home allUserIngredients: ${JSON.stringify(this.allUserIngredients)}`
    );
    this.getRecipesBasedOnIngredients(); 
  }

  convertToRecipeCards(recipeList: Recipe[]): RecipeCard[] {
    return recipeList.map((recipeMatched) => ({
      recipe: recipeMatched,
      enoughQuantity: true, // field not used in home but in frigo
    }));
  }

  getAllIngredients(): void {
    this.ingredientService.getAllIngredients().subscribe({
      next: (ingredients) => {
        localStorage.setItem('allIngredients', JSON.stringify(ingredients));
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des ingrédients', err);
      },
    });
  }

  getRecipesBasedOnIngredients(): void {
    const allIngredients: IngredientBack[] =
      this.getIngredientsFromLocalStorage();

    const userSelectedIngredients: IngredientBack[] = this.allUserIngredients
      .map((ingredientName) =>
        allIngredients.find(
          (ingredient) => ingredient.name === ingredientName.name
        )
      )
      .filter((ingredient) => ingredient !== undefined && ingredient !== null); // Filtrer les éléments non trouvés

    this.recipeService
      .getRecipesBasedOnIngredients(userSelectedIngredients)
      .subscribe({
        next: (recipes) => {
          this.allRecipeCardsData = this.convertToRecipeCards(recipes);
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des ingrédients', err);
        },
      });
  }

  getIngredientsFromLocalStorage(): IngredientBack[] {
    const storedIngredients = localStorage.getItem('allIngredients');

    if (storedIngredients) {
      return JSON.parse(storedIngredients) as IngredientBack[];
    }
    return [];
  }
}
