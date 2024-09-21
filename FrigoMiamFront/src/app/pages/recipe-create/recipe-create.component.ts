import { Component } from '@angular/core';

import {FormArray, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CreateRecipeService} from "../../services/create-recipe.service";
import {AuthService} from "../../services/auth.service";


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
    ingredients: new FormArray([
      new FormGroup({
        ingredient: new FormControl(''),
        quantity: new FormControl('')
      })
    ])
    });

  dietList=[
    { label : 'végétalien', value:' VEGETARIAN'},
    { label: 'vegan', value:'VEGAN'},
    { label :"pescetarien", value:'PESCATARIAN'}]

  isSubmitted = false;


  constructor(
    private createRecipeService: CreateRecipeService,
    private authService: AuthService
  ) { }


  addIngredient() {
    const addQtArray = this.createRecipe.get('ingredients') as FormArray;
    addQtArray.push(new FormGroup({
      ingredient: new FormControl(''),
      quantity: new FormControl('')
    }));
    }

  submitRecipe() {
    //console.log(`Recette soumise: ${JSON.stringify(this.createRecipe.value)}`, );
    //const user = this.authService.getCurrentUser();//implément authService

    //create object JSOn
    const recipePayload = {
      recipe:{
        title: this.createRecipe.value.titleRecipe,
        description: this.createRecipe.value.description,
        instructions: this.createRecipe.value.instructions,
        preparation_time: this.createRecipe.value.preparation_time,
        cooking_time:this.createRecipe.value.cooking_time,
        calories: this.createRecipe.value.calories,
        typeRecipe: 'STARTER',
        validationMessage: 'VALIDATED',
        diet: this.createRecipe.value.diet,
      },
      ingredients: this.createRecipe.value.ingredients?.map((ingredient:any)=> ({
        ingredient:{
          id: ingredient.id,
          name: ingredient.name,
          unit:ingredient.unit,
          typeIngredient: ingredient.typeIngredient,
          allergy: ingredient.allergy,
        },
        quantity: ingredient.quantity
      })),
      // account: {
      //   id: user.id,
      //   firstname: user.firstname,
      //   lastname: user.lastname,
      //   email: user.email,
      //   password: user.password,
      //   birthdate: user.birthdate,
      //
      // }
    };
    //send req service
    this.createRecipeService.createRecipe(recipePayload).subscribe({
      next: (response) => {
        console.log('Votre recette à été créé avec succés', response);
        this.isSubmitted = true;
      },
      error:(err) => {
        console.error(' Il y a une erreur lors de la création de votre recette', err)
      }
    });


  }
}

