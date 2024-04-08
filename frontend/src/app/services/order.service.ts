import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  private apiUrl = 'http://localhost:8080/orders';

  constructor(private http: HttpClient) { }

  getAllOrders(): Observable<any[]> {
    const headers = this.getHeaders();
    console.log(headers);
    return this.http.get<any[]>(`${this.apiUrl}/all`, { headers });
  }

  getOrderDetails(orderId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${this.apiUrl}/${orderId}`, { headers });
  }

  cancelOrder(orderId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.patch(`${this.apiUrl}/${orderId}/cancel`, {}, { headers, responseType: 'text'});
  }

  completeOrder(orderId: number): Observable<string> {
    const headers = this.getHeaders();
    return this.http.patch(`${this.apiUrl}/${orderId}/complete`, {}, { headers, responseType: 'text'});
  }

  private getHeaders(): HttpHeaders {
    const authToken = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${authToken}`
    });
  }
}
