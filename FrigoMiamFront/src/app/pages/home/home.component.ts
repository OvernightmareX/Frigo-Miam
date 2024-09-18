import { Component } from '@angular/core';
import {IngredientSearchComponent} from "../../components/ingredient-search/ingredient-search.component";
import {IngredientListComponent} from "../../components/ingredient-list-home/ingredient-list.component";
import {RecipeCardShortComponent} from "../../components/recipe-card-short/recipe-card-short.component";
import {IngredientBack, Recipe, RecipeCard, RecipeMatched} from "../../utils/types";
import {IngredientClientService} from "../../services/http/ingredient/ingredient-client.service";

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

  ingr: IngredientBack[] = []

  constructor(private ingredientService: IngredientClientService) {
    this.getAllIngredients()
  }

  allRecipeCardsData: RecipeCard[] = [];
  allUserIngredients: string[] = [];

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
      "ingredients": ["aaa", "aaaa"],
      "nom": "Recette 5",
      "description": "15eme recette..."
    },
    {
      "ingredients": ["aa"],
      "nom": "Recette 6",
      "description": "6eme recette..."
    }
  ]

  addIngredient(addedIngredient: string): void {  // TODO a mettre dans util
    this.allUserIngredients.push(addedIngredient);
    this.allRecipeCardsData = this.recipeIngredientMatching();
  }


  // find all recipe that CONTAINS the ingredient list
  recipeIngredientMatching(): RecipeCard[]{
    let recipesFiltered: RecipeMatched[] = [];

    this.recipes.forEach(baseRecepe => {                          // pour toutes les recettes de base

      this.allUserIngredients.forEach(userIngredient => {         // on regarde pour chaque ingrédient ajouté par l'utilisateur
        if (baseRecepe.ingredients.includes(userIngredient)) {    // s'il est contenu dans la liste des ingrédients de la recette

          const foundRecipe = recipesFiltered.find(recepeFound => recepeFound.recepe === baseRecepe);   // alors on regarde dans notre liste de recette qui match si recep existe dedans
          if(foundRecipe) {                                       // si la recette est déjà ajouté à la liste des recettes qui match
            foundRecipe.commonIngredientCount += 1;               // on ajoute 1 à son nombre d'ingrédient qui match
          } else {                                                // sinon on ajoute la recette à la liste des recettes qui match
            recipesFiltered.push({
              "commonIngredientCount": 1,
              "recepe": baseRecepe,
            });
          }
        }
      });
    });
    // on a donc récupérer toutes les recettes ayant au moins un ingrédient qui match

    recipesFiltered = recipesFiltered.sort((a, b) => b.commonIngredientCount - a.commonIngredientCount);   // on trie par nombre d'ingrédients qui match.
    recipesFiltered.length = 5;  // on garde que les 5 ayant le plus d'ingrédients qui match

    return this.convertToRecipeCards(recipesFiltered);
  }


  convertToRecipeCards(recipeList: RecipeMatched[]): RecipeCard[] {
    return recipeList.map(recipeMatched => ({
      nom: recipeMatched.recepe.nom,
      description: recipeMatched.recepe.description,
      enoughQuantity: true  // field not used in home but in frigo
    }));
  }

  getAllIngredients(): void{
    this.ingredientService.getAllIngredients().subscribe({
      next: ingredients => {
        localStorage.setItem('allIngredients', JSON.stringify(ingredients));
      },
      error: err => {
        console.error('Erreur lors de la récupération des ingrédients', err);
      }

    })
  }



/*
  // find all recipe that have STRICTLY the ingredient list
  recipeIngredientMatchingStrict(): RecipeCard[]{
    const recipesFiltered: Recipe[] = [];

    this.recipes.forEach((recep) => {
      if (this.allUserIngredients.every(r => recep.ingredients.includes(r))){
        recipesFiltered.push(recep);
      }
    })

    return this.convertToRecipeCards(recipesFiltered);
  }

 */
}
