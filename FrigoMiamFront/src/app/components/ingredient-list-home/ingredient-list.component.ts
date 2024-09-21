import {Component, Input} from '@angular/core';
import {IngredientBack, IngredientRecipe} from "../../utils/types";

@Component({
  selector: 'app-ingredient-list-home',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-list.component.html',
  styleUrl: './ingredient-list.component.css'
})
export class IngredientListComponent {

  @Input() ingredientList ?: IngredientBack[];

}
