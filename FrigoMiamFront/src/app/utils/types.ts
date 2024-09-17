export type Recipe = {
  ingredients: string[],
  nom: string,
  description: string
};

export type RecipeCard = {
  nom: string,
  description: string,
  enoughQuantity: boolean
}

export type RecipeMatched = {
  "commonIngredientCount": number,
  "recepe": Recipe
}

export type User = {
  email: string,
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
