import { Component } from '@angular/core';
import {IngredientListComponent} from "../../components/ingredient-list-home/ingredient-list.component";
import {IngredientClientService} from "../../services/http/ingredient/ingredient-client.service";
import {ActivatedRoute} from "@angular/router";
import {IngredientBack} from "../../utils/types";

@Component({
  selector: 'app-recette',
  standalone: true,
  imports: [
    IngredientListComponent
  ],
  templateUrl: './recette.component.html',
  styleUrl: './recette.component.css'
})
export class RecetteComponent {

  inputValue?: number = 4;
  ingredientRecipeList?: string[] = [];
  recipeDescription: string = "Ma description n'est pas super longue mais elle habille la page."
  recipeName?: string | null | undefined;
  userNote?: number | null | undefined;
  averageNote?: number | null | undefined = 4;

  constructor(
    private ingredientService: IngredientClientService,// TODO change with RecipeService
    private route: ActivatedRoute) {}

  ngOnInit() {
    this.recipeName = this.route.snapshot.paramMap.get('name');
    console.log(`recipeName: ${this.recipeName}`);
  }

}
