import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, catchError, Observable, of, tap} from 'rxjs';
import {User} from "../utils/types";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  private authentifiedSubject = new BehaviorSubject<boolean>(false);  // Default is false
  public authentified$ = this.authentifiedSubject.asObservable();  // Expose the observable

  login(cred: Pick<User, 'email' | 'password'>): Observable<{ token: string }> {
    console.log("arrived in login")
    return this.http.post<{ token: string }>(`${this.apiUrl}/account/login`, cred).pipe(
      tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('email', cred.email);
        localStorage.setItem('password', cred.password);
        this.authentifiedSubject.next(true);
      }),
      catchError(error => {
        alert(error.message);
        return of({token:''});
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    localStorage.removeItem('password');
    this.authentifiedSubject.next(false);  // Set the boolean to false on logout
  }

}


// register(user: Partial<User>): Observable<User> {
//   return this.http.post<User>(`${this.apiUrl}/account`, user).pipe(
//     catchError(error => {
//       alert(error.message);
//       return of({} as User);
//     })
//   );
// }




