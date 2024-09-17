import { Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {AdminComponent} from "./pages/admin/admin.component";
import {ProfilComponent} from "./pages/profil/profil.component";
import {ConnexionComponent} from "./pages/connexion/connexion.component";
import {FrigoComponent} from "./pages/frigo/frigo.component";
import {AccountCreationComponent} from "./pages/account-creation/account-creation.component";
import {RecetteComponent} from "./pages/recette/recette.component";
import {NotFound404Component} from "./pages/not-found404/not-found404.component";
import {NgModule} from "@angular/core";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'admin', component: AdminComponent},
  {path: 'profile', component: ProfilComponent},
  {path: 'connexion', component: ConnexionComponent},
  {path: 'frigo', component: FrigoComponent},
  {path: 'inscription', component: AccountCreationComponent},
  {path: 'recette/:name', component: RecetteComponent}, //
  {path: '**' , component:NotFound404Component},
];


