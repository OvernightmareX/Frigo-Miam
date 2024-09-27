import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../services/http/account/auth.service";

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
  isHovered: boolean = false;


  constructor(private authService: AuthService, private router: Router) {
    this.authService.authentified$.subscribe(isAuth => {
      this.isAuthentified = isAuth;
    });
  }

  toggleHover() {
    this.isHovered = !this.isHovered;
  }

  logout(): void {
    this.authService.logout();
  }

}
