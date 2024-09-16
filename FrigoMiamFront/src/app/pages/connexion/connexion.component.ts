import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-connexion',
  standalone: true,
  imports: [
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './connexion.component.html',
  styleUrl: './connexion.component.css'
})
export class ConnexionComponent {
  formConnect = new FormGroup({
    email: new FormControl('',[Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
    });
  constructor(private authService: AuthService,
              private router: Router) {}

connectSubmit() {
  const credentials = this.formConnect.value as Pick<User, 'email' | 'password'>;

  this.authService.login(credentials).subscribe({
    next: res => {
      if (res) {
        this.router.navigate(['/']);
      }
    },
    error: err => {
      console.error(err);
      alert('login failed' + err.message);
    }
  });

}




subscribeAccount() {
this.router.navigate(['/inscription']);
}
}
