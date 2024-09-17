import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {UserService} from "../../services/user.service";

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
  dietList=[
    { value :'vegetarien' ,label:'vegetarien'},
    { value : 'végétalien', label:' végétalien '},
    { value: 'cétogène', label:'cétogène'},
    { value :"flexitarien", label:'flexitarien'},
    { value : 'paléolithique', label:'paléolithique'},
    { value: 'hypocalorique',label:'hypocalorique'},
  ]

  allergenList=[
    { value :'sans lactose' ,label:'sans lactose'},
    { value :'sans sucre', label:'sans sucre'},
    { value:'sans gluten', label:'sans gluten'},

  ]

user ={
  lastname:'',
  firstName: '',
  birthdate:'',
  email:'',
  diets:this.dietList ,
  allergies:this.allergenList,
  password:'',
  confirmPassword:'',
}

isSubmitted = false;

constructor(private userService: UserService) {}

  get passwordHasError(){
return this.isSubmitted && this.user.password.length <5;
}

  get confirmPasswordHasError(){
    return this.isSubmitted && this.user.password !== this.user.confirmPassword;

  }
  submitAccount(){
    this.isSubmitted = true;
    if (!this.passwordHasError && !this.confirmPasswordHasError){
      this.userService.setUser(this.user);
      console.log(this.user)
    }

}

}
