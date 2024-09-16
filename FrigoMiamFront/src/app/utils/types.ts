export type Recipe = {
  ingredients: string[],
  nom: string,
  description: string
};

export type RecipeCard = {
  nom: string,
  description: string
}

export type RecipeMatched = {
  "commonIngredientCount": number,
  "recepe": Recipe
}
