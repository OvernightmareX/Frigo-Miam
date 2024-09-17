import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, Observable, of, tap } from 'rxjs';
import {User} from "../models/user";

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
        return of();
      })
    );
  }


  login(cred: Pick<User, 'email' | 'password'>): Observable<{ accessToken: string }> {
    return this.http.post<{ accessToken: string }>(`${this.apiUrl}/login`, cred).pipe(
      tap(res => localStorage.setItem('token', res.accessToken)),
      catchError(error => {
        alert(error.message);
        return of();
      })
    );
  }


  getToken(): string | null {
    return localStorage.getItem('token');
  }
}



