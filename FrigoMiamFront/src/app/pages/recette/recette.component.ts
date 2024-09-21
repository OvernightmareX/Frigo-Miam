import { Component } from '@angular/core';
import { IngredientListComponent } from '../../components/ingredient-list-home/ingredient-list.component';
import { IngredientClientService } from '../../services/http/ingredient/ingredient-client.service';
import { ActivatedRoute } from '@angular/router';
import { IngredientBack, IngredientRecipe, Recipe } from '../../utils/types';
import { RecipeDetailsService } from '../../services/http/recipes/recipe-details.service';

@Component({
  selector: 'app-recette',
  standalone: true,
  imports: [IngredientListComponent],
  templateUrl: './recette.component.html',
  styleUrl: './recette.component.css',
})
export class RecetteComponent {
  recipe: Recipe = {
    id: '',
    title: '',
    description: '',
    instructions: '',
    preparation_time: 0,
    cooking_time: 0,
    calories: 0,
    typeRecipe: '',
    diet: '',
    validation: '',
    recipeIngredientsList: [],
  };

  ingredientsList: IngredientRecipe[] = [];

  inputValue?: number = 4;

  constructor(
    private recipeDetailService: RecipeDetailsService, // TODO change with RecipeService
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const recipeId = this.route.snapshot.paramMap.get('id');
    if (recipeId) {
      this.recipeDetailService.getRecipeInfo(recipeId).subscribe({
        next: (data) => {
          this.recipe = data;
          this.ingredientsList = data.recipeIngredientsList;
        },
        error: (error) => {
          console.error(error);
        },
      });
    } else {
      console.error('No recipeId found');
    }
  }
}
