import { Component } from '@angular/core';

import {FormArray, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";


@Component({
  selector: 'app-recipe-create',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './recipe-create.component.html',
  styleUrls: ['./recipe-create.component.css']
})
export class RecipeCreateComponent {

  createRecipe = new FormGroup({
    titleRecipe: new FormControl(''),
    description: new FormControl(''),
    instructions: new FormControl(''),
    preparation_time: new FormControl (''),
    cooking_time: new FormControl(''),
    calories: new FormControl(''),
    diet: new FormControl(''),
    recipeIngredientsList: new FormArray([
      new FormGroup({
        ingredient: new FormControl(''),
        quantity: new FormControl('')
      })
    ])
    });

  dietList=[  { label : 'végétalien', value:' VEGETARIAN'},  { label: 'vegan', value:'VEGAN'},  { label :"pescetarien", value:'PESCATARIAN'}]

  isSubmitted = false;


  constructor() { }


  addIngredient() {

    const addQtArray = this.createRecipe.get('recipeIngredientsList') as FormArray;
    addQtArray.push(new FormGroup({
      ingredient: new FormControl(''),
      quantity: new FormControl('')
    }));



    }


  submitRecipe() {
    console.log(`Recette soumise: ${JSON.stringify(this.createRecipe.value)}`, );

    this.isSubmitted = true;
  }
}

