import {Component, Input} from '@angular/core';
import {RecipeCard} from "../../utils/types";
import {Router} from "@angular/router";
import { RecipeDetailsService } from '../../services/http/recipes/recipe-details.service';

@Component({
  selector: 'app-recipe-card-short',
  standalone: true,
  imports: [],
  templateUrl: './recipe-card-short.component.html',
  styleUrl: './recipe-card-short.component.css'
})
export class RecipeCardShortComponent {

  @Input() recipeCardData ?: RecipeCard;

  constructor(private router: Router, private recipeService: RecipeDetailsService) {}

  navigateToRecipe(): void{
    if (this.recipeCardData) {
    this.recipeService.recipeId = this.recipeCardData.recipe.id;
      this.router.navigate(['/recette', this.recipeCardData.recipe.title]);   // TODO a tester quand on aura des recettes, faudra remettre le paramètre dans app.routes.ts
    }
  }
}
