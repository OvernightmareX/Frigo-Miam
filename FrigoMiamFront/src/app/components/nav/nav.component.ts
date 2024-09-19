import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../services/auth.service";

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
  isAuthentified: boolean = false;


  constructor(private authService: AuthService, private router: Router) {
    this.authService.authentified$.subscribe(isAuth => {
      this.isAuthentified = isAuth;
    });
  }

  toggleMenu() {
    if (this.isAuthentified){
      console.log("nav authentifié")
    }else {
      this.router.navigate(['/connexion']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }


  // get checkAuthentification() {
  //   return this.isAuthentified = localStorage.getItem("isAuthentified");
  // }
}
