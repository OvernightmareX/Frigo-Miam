import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IngredientBack, IngredientQuantity } from '../../utils/types';

@Component({
  selector: 'app-ingredient-list-home',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-list.component.html',
  styleUrl: './ingredient-list.component.css',
})
export class IngredientListComponent {
  @Input() ingredientList?: IngredientBack[];
  @Output() ingredientDeleted = new EventEmitter<number>();

  deleteIngredient(index: number){
    this.ingredientDeleted.emit(index);
  }
}
