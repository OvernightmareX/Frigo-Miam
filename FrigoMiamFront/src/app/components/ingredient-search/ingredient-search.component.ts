import { Component } from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";

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

  ingredient_control = new FormControl(``, [
    Validators.required,
    Validators.pattern(/^[A-Za-z'].*/)
  ]);

  constructor() {
    this.ingredient_control.valueChanges.subscribe(value => {
      this.filterPossibilities(value);
    });
  }

  possibilityList = [
    'a',
    'aa',
    'aaa',
    'aaaa'
  ]
  filteredPossibilities: string[] = [];



  filterPossibilities(value: string) {
    if (value) {
      this.filteredPossibilities = this.possibilityList.filter(item =>
        item.toLowerCase().startsWith(value.toLowerCase())
      );
    } else {
      this.filteredPossibilities = [];
    }
  }

  addIngredient(){
    console.log(this.ingredient_control.value);
  }

}
