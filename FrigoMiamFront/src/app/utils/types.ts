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

export type IngredientFrigo = {
  "name": string,
  "quantity": number
}
