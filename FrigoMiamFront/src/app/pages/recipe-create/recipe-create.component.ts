import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

@Component({
  selector: 'app-recipe-create',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './recipe-create.component.html',
  styleUrls: ['./recipe-create.component.css']
})
export class RecipeCreateComponent {

  recipe = {
    title: '',
    ingredients: [] as { ingredient: string, quantity: number, value: number }[],
    descript: '',
    urlImg: '',
  };

  isSubmitted = false;

  newIngredient: { ingredient: string; quantity: number } = { ingredient: '', quantity: 1 };

  constructor() { }

  addIngredient() {
    if (this.newIngredient.ingredient && this.newIngredient.quantity > 0) {
      //this.recipe.ingredients.push({ ...this.newIngredient });
      this.newIngredient = { ingredient: '', quantity: 1 };
    }
  }

  submitRecipe() {
    this.isSubmitted = true;
    console.log('Recette soumise', this.recipe);
  }
}

