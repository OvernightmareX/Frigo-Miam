import { Routes } from '@angular/router';
import {HomeComponent} from "./pages/home/home.component";
import {AdminComponent} from "./pages/admin/admin.component";
import {ProfilComponent} from "./pages/profil/profil.component";
import {ConnexionComponent} from "./pages/connexion/connexion.component";
import {FrigoComponent} from "./pages/frigo/frigo.component";
import {AccountCreationComponent} from "./pages/account-creation/account-creation.component";
import {RecetteComponent} from "./pages/recette/recette.component";

export const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'admin', component: AdminComponent},
  {path: 'profile', component: ProfilComponent},
  {path: 'connexion', component: ConnexionComponent},
  {path: 'frigo', component: FrigoComponent},
  {path: 'creation', component: AccountCreationComponent},
  {path: 'recette', component: RecetteComponent}
];
