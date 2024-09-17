import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import {User} from "../utils/types";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}


  register(user: Partial<User>): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/account`, user).pipe(
      catchError(error => {
        alert(error.message);
        return of({} as User);
      })
    );
   }


  login(cred: Pick<User, 'email' | 'password'>): Observable<{ accessToken: string }> {
    console.log("arrived in login")
    return this.http.post<{ accessToken: string }>(`${this.apiUrl}/login`, cred).pipe(
      tap(res => localStorage.setItem('token', res.accessToken)),
      catchError(error => {
        alert(error.message);
        return of({accessToken:''});
      })
    );
  }


  getToken(): string | null {
    return localStorage.getItem('token');
  }
}



