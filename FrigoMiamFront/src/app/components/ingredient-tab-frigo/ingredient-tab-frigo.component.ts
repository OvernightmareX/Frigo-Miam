import { Component, EventEmitter, Input, Output } from '@angular/core';
import { IngredientQuantity } from '../../utils/types';

@Component({
  selector: 'app-ingredient-tab-frigo',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-tab-frigo.component.html',
  styleUrl: './ingredient-tab-frigo.component.css',
})
export class IngredientTabFrigoComponent {
  @Input() ingredientsInFrigo: IngredientQuantity[] = [];
  @Output() deleteIngredientEvent = new EventEmitter<IngredientQuantity>();
  @Output() quantityChange = new EventEmitter<{
    quantity: number;
    ingredientChange: IngredientQuantity;
  }>();

  deleteIngredient(ingredientToDelete: IngredientQuantity): void {
    this.deleteIngredientEvent.emit(ingredientToDelete);
  }

  onQuantityChange(event: Event, ingredientChange: IngredientQuantity): void {
    const quantity: number = +(event.target as HTMLInputElement).value;
    this.quantityChange.emit({ quantity, ingredientChange });
  }
}
