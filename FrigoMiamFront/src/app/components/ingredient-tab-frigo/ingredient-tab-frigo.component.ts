import {Component, EventEmitter, Input, Output} from '@angular/core';
import {IngredientFrigo} from "../../utils/types";

@Component({
  selector: 'app-ingredient-tab-frigo',
  standalone: true,
  imports: [],
  templateUrl: './ingredient-tab-frigo.component.html',
  styleUrl: './ingredient-tab-frigo.component.css'
})
export class IngredientTabFrigoComponent {



  @Input() ingredientsInFrigo: IngredientFrigo[] = [];
  @Output() deleteIngredientEvent = new EventEmitter<IngredientFrigo>;
  @Output() quantityChange = new EventEmitter<{quantity: number, ingredientChange: IngredientFrigo}>

  deleteIngredient(ingredientToDelete: IngredientFrigo): void {
    this.deleteIngredientEvent.emit(ingredientToDelete)
  }

  onQuantityChange(event: Event, ingredientChange: IngredientFrigo): void{
    const quantity: number = +(event.target as HTMLInputElement).value;
    this.quantityChange.emit({quantity, ingredientChange});
  }

}

