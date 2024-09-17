import { Component } from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../services/auth.service";
import {Router} from "@angular/router";
import {User} from "../../utils/types";

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})
export class AdminComponent {

  formConnect = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });

  constructor(private authService: AuthService,
              private router: Router) {}

  connectSubmit() {
    console.log(`arrived in connect submit`)
    const credentials = this.formConnect.value as Pick<User, 'email' | 'password'>;
    console.log(`credentials : ${credentials}`)

    this.authService.login(credentials).subscribe({
      next: res => {
        console.log(`after next`)
        if (res) {
          this.router.navigate(['/']);
        }
      },
      error: err => {
        console.error(err);
        alert('login failed: ' + err.message);
      }
    });
  }

  subscribeAccount() {
    this.router.navigate(['/inscription']);
  }
}
