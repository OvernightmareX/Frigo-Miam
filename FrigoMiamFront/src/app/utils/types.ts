export type Recipe = {
  ingredients: string[],
  nom: string,
  description: string
};

export type RecipeDetails = {
  id: string,
  title: string,
  description: string,
  instructions: string,
  preparation_time: number,
  cooking_time: number,
  calories: number,
  typeRecipe: string,
  diet: string,
  validation: string,
  ingredients: IngredientBack[]
}

export type RecipeCard = {
  id: string,
  nom: string,
  description: string,
  enoughQuantity: boolean
}

export type RecipeMatched = {
  "commonIngredientCount": number,
  "recepe": Recipe
}

export class User {

  constructor(email: string,
              password: string,
              lastname?: string,
              firstname?: string,
              birthdate?: Date,
              diet?: string,
              allergies?: string[]) {
    this.lastname = lastname;
    this.firstname = firstname;
    this.birthdate = birthdate;
    this.email = email;
    this.diet = diet;
    this.allergies = allergies;
    this.password = password;
  }

  lastname?: string
  firstname?: string
  birthdate?: Date
  email: string
  diet?: string
  allergies?: string[]
  password: string
}

export type IngredientFrigo = {
  "name": string,
  "quantity": number
}

export type IngredientBack = {
  "id": string,
  "name": string,
  "unit": string,
  "typeIngredient":string,
  "allergy": string | null,
  "accountIngredientsList": string[],  // may be blocking at a moment or another
  "recipeIngredientsList": string[]  // may be blocking at a moment or another
}



// allergy
//     DAIRY, NUTS, GLUTEN, EGGS, FISH, SEAFOOD, SOY

// Diet
//     VEGETARIAN, VEGAN, PESCATARIAN

// typeIngredient
//    MEAT, FISH, VEGETABLE, FRUIT, SPICE, HERB, SEAFOOD, PASTA, RICE, CEREALS, CHEESE, DAIRY

// typeRecipe
//     STARTER, MAIN_COURSE, DESSERT

// Unit
//    GR, KG, CL, ML, L, PINCH, UNIT

// validation
//     VALIDATED, INVALIDATED, PENDING
