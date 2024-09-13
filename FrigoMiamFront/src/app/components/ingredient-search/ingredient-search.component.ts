import { Component } from '@angular/core';
import {FormControl, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {of} from "rxjs";

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
      console.log(`constructor: ${value}`);
      this.filterPossibilities(value ?? '');
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
    console.log(`filterPossibilities: ${value}`);
    if (value) {
      this.filteredPossibilities = this.possibilityList.filter(item =>
        item.toLowerCase().startsWith(value.toLowerCase())
      );
    } else {
      this.filteredPossibilities = [];
    }
  }

  addIngredient(){
    console.log(`addIngredient: ${this.ingredient_control.value}`);
  }

  protected readonly of = of;
}
