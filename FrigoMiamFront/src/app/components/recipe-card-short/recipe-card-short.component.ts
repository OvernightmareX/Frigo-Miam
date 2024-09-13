import {Component, Input} from '@angular/core';
import {RecipeCard} from "../../utils/types";

@Component({
  selector: 'app-recipe-card-short',
  standalone: true,
  imports: [],
  templateUrl: './recipe-card-short.component.html',
  styleUrl: './recipe-card-short.component.css'
})
export class RecipeCardShortComponent {
  @Input() recipeCardData ?: RecipeCard;
}
