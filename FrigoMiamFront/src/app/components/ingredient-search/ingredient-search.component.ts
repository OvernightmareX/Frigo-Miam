import {Component, EventEmitter, Output} from '@angular/core';
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

  @Output() clickEvent = new EventEmitter<string>;  // a renommer

  constructor() {
    this.ingredient_control.valueChanges.subscribe(value => {
      this.filterPossibilities(value ?? '');
    });
  }

  possibilityList = [ // TODO: will be replaced by values in service or localStorage
    'a',
    'aa',
    'aaa',
    'aaaa'
  ]
  filteredPossibilities: string[] = [];

  ingredient_control = new FormControl(``, [
    Validators.required,
    Validators.pattern(/^[A-Za-z'].*/)
  ]);

  addIngredient(){
    if(this.ingredient_control.valid){
      this.clickEvent.emit(this.ingredient_control.value ?? '');
    }
  }

  filterPossibilities(value: string) {
    if (value) {
      this.filteredPossibilities = this.possibilityList.filter(item =>
        item.toLowerCase().startsWith(value.toLowerCase())
      );
    } else {
      this.filteredPossibilities = [];
    }
  }

  protected readonly of = of;
}
