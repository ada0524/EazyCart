import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {
  private apiUrl = 'http://localhost:8080/watchlist';

  constructor(private http: HttpClient) { }

  private getHeaders(): HttpHeaders {
    const authToken = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${authToken}`
    });
  }

  getWatchlist(): Observable<any[]> {
    const headers = this.getHeaders();
    console.log(headers);
    return this.http.get<any[]>(`${this.apiUrl}/products/all`, { headers });
  }

  addToWatchlist(productId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.post(`${this.apiUrl}/product/${productId}`, {}, { headers, responseType: 'text'});
  }

  removeFromWatchlist(productId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.delete<string>(`${this.apiUrl}/product/${productId}`, { headers, responseType: 'text' as 'json' });
  }
}
