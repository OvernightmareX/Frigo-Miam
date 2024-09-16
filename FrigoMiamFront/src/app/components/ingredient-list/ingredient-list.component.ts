import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-ingredient-list',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-list.component.html',
  styleUrl: './ingredient-list.component.css'
})
export class IngredientListComponent {

  @Input() ingredientList ?: string[];

}
