import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as jwt from 'jsonwebtoken';

// const options = { withCredentials: true };

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:9999';
  private jwtSecretKey = 'onlineShoppingApp';

  constructor(private http: HttpClient) { }

  signup(username: string, email: string, password: string): Observable<{ message: string }> {
    const body = { username, email, password };
    return this.http.post<{ message: string }>(`${this.apiUrl}/signup`, body);
  }

  login(username: string, password: string): Observable<{ message: string, token: string }> {
    const body = { username, password };
    return this.http.post<{ message: string, token: string }>(`${this.apiUrl}/login`, body);
  }

  storeToken(token: string): void {
    localStorage.setItem('authToken', token);
  }

  getStoredToken(): string | null {
    return localStorage.getItem('authToken');
  }

  isUser(): boolean {
    const jwtToken = this.getStoredToken();

    if (jwtToken) {
      try {
        // const decodedToken: any = jwt.verify(jwtToken, this.jwtSecretKey);
        // const userName: string = decodedToken.sub;
        const userName = "admin"
        return userName.startsWith('user');
      } catch (error) {
        console.error('Error parsing JWT token:', error);
        return false;
      }
    }

    return false;
  }
}
