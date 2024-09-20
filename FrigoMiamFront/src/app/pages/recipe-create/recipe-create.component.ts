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
    addQt: new FormArray([
      new FormGroup({
        ingredient: new FormControl(''),
        quantity: new FormControl('')
      })
    ])
    });

  // recipe = {
  //   title: '',
  //   descript: '',
  //   instructions:'',
  //   preparation_time:'',
  //   cooking_time:'',
  //   diet:[],
  //   ingredients: [] as { ingredient: string, quantity: number, value: number }[],
  //
  //
  // };

  isSubmitted = false;

  //newIngredient: { ingredient: string; quantity: number, value: number } = { ingredient: '', quantity: 1, value: 2 };

  constructor() { }


  addIngredient() {

    const addQtArray = this.createRecipe.get('addQt') as FormArray;
    addQtArray.push(new FormGroup({
      ingredient: new FormControl(''),
      quantity: new FormControl('')
    }));


    // console.log("In addIngredient")
    // //if (this.createRecipe && this.newIngredient.quantity > 0) {
    // //console.log("PROCESS addIngredient - after if")
    //
    // //this.recipe.ingredients.push({ ...this.newIngredient });
    // //this.newIngredient = { ingredient: '', quantity: 1, value: 2 };
    // this.createRecipe.controls.addQt.push(
    //   new FormGroup({
    //     ingredient: new FormControl(''),
    //     quantity: new FormControl('')
    //   })
    // )
    }


  submitRecipe() {
    console.log(`Recette soumise: ${JSON.stringify(this.createRecipe.value)}`, );

    this.isSubmitted = true;
  }
}

