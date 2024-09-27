import { Component } from '@angular/core';
import { IngredientListComponent } from '../../components/ingredient-list-home/ingredient-list.component';
import { ActivatedRoute } from '@angular/router';
import { IngredientQuantity, Recipe } from '../../utils/types';
import { RecipeDetailsService } from '../../services/http/recipes/recipe-details.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-recette',
  standalone: true,
  imports: [IngredientListComponent, FormsModule],
  templateUrl: './recette.component.html',
  styleUrl: './recette.component.css',
})
export class RecetteComponent {
  recipe: Recipe = {
    id: '',
    title: '',
    imageUrl: '', 
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

  ingredientsList: IngredientQuantity[] = [];
  recipeGrade:number = 0;

  inputValue: number = 1;

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
      this.recipeDetailService.getAverageGrade(recipeId).subscribe({
        next: (data) => {
          this.recipeGrade = data;
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
