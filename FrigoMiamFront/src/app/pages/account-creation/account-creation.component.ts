import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../../services/http/account/user.service";
import {User} from "../../utils/types";
import { Router } from '@angular/router';
import {allergenList, dietList} from "../../utils/enums";

@Component({
  selector: 'app-account-creation',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    FormsModule
  ],
  templateUrl: './account-creation.component.html',
  styleUrl: './account-creation.component.css'
})
export class AccountCreationComponent {

  user = {
    lastname:'',
    firstname: '',
    birthdate:'',
    email:'',
    diet:'',
    allergies:[],
    password:'',
    confirmPassword:'',
  }

  isSubmitted = false;

  constructor(private userService: UserService, private router: Router) {}

  get passwordHasError(){
    return this.isSubmitted && this.user.password.length <5;
  }

  get confirmPasswordHasError(){
    return this.isSubmitted && this.user.password !== this.user.confirmPassword;
  }
  submitAccount(){

    this.isSubmitted = true;
    if (!this.passwordHasError && !this.confirmPasswordHasError){

      console.log(`gna: ${JSON.stringify(this.user)}`)

      const userInstance = new User(
        this.user.email,
        this.user.password,
        this.user.lastname,
        this.user.firstname,
        new Date(this.user.birthdate),
        this.user.diet,
        this.user.allergies
      );

      console.log(`userIstance: ${JSON.stringify(userInstance)}`)

      this.userService.createUser(userInstance).subscribe({
        next: userFromBack => {
          console.log(`userFromBack: ${JSON.stringify(userFromBack)}`);
          this.router.navigate(['/connexion'])
        },
        error: err => {
          console.error("Erreur lors de la création de l'utilisateur", err);
        }
      })
    }
    this.isSubmitted = false;
  }

  protected readonly allergenList = allergenList;
  protected readonly dietList = dietList;
}
