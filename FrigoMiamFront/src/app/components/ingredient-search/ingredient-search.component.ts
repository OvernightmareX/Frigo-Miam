import {Component, EventEmitter, Output} from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {of} from "rxjs";
import {IngredientBack} from "../../utils/types";

@Component({
  selector: 'app-ingredient-search',
  standalone: true,
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './ingredient-search.component.html',
  styleUrl: './ingredient-search.component.css'
})
export class IngredientSearchComponent {

  @Output() clickEvent = new EventEmitter<IngredientBack>;  // TODO a renommer

  ingredientsStored: IngredientBack[] = []
  filteredPossibilities: IngredientBack[] = [];
  selectedIngredient?: IngredientBack = undefined;

  constructor() {
    this.ingredient_control.valueChanges.subscribe(value => {
      this.filterPossibilities(value ?? '');
    });

    this.ingredientsStored = this.getIngredientsFromLocalStorage();
  }


  ingredient_control = new FormControl(``, [
    Validators.required,
    Validators.pattern(/^[A-Za-z'].*/)
  ]);

  ingredientSelected: IngredientBack[] = [];

  addIngredient() {
    if (this.selectedIngredient && this.ingredient_control.value != null) {

      this.ingredientSelected = this.ingredientsStored.filter(item =>
        item.name.toLowerCase() === this.ingredient_control.value?.toLowerCase()
      );
      this.clickEvent.emit(this.ingredientSelected[0]);

      this.selectedIngredient = undefined
    } else {
      console.log('Aucun ingrédient sélectionné ou ingrédient non trouvé');
    }
  }

  onIngredientSelect() {
    const selectedName = this.ingredient_control.value;
    this.selectedIngredient = this.ingredientsStored.find(ingredient => ingredient.name === selectedName);
  }

  filterPossibilities(value: string) {

    if (value) {
      this.filteredPossibilities = this.ingredientsStored.filter(item =>
        item.name.toLowerCase().startsWith(value.toLowerCase())
      );
    } else {
      this.filteredPossibilities = [];
    }
  }

  getIngredientsFromLocalStorage(): IngredientBack[] {
    const storedIngredients = localStorage.getItem('allIngredients');

    if (storedIngredients) {
      return JSON.parse(storedIngredients) as IngredientBack[];
    }
    return [];
  }

  protected readonly of = of;
}
