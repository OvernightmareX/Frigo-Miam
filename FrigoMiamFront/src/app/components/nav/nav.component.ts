import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-nav',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './nav.component.html',
  styleUrl: './nav.component.css'
})
export class NavComponent {
  isUserMenuOpen = false;
  isAuthentified: string | null = null;

  constructor(private router: Router) {

  }

  toggleMenu() {
    if (this.isAuthentified){
      this.isUserMenuOpen = !this.isUserMenuOpen;
    }else {
      this.router.navigate(['/connexion']);
    }

  }


  get checkAuthentification() {
    return this.isAuthentified = localStorage.getItem("isAuthentified");
  }
}
