import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = 'http://localhost:8080/products';

  constructor(private http: HttpClient) { }

  getAllProducts(): Observable<any[]> {
    const headers = this.getHeaders();
    return this.http.get<any[]>(`${this.apiUrl}/all`, { headers });
  }

  getProductDetails(productId: number): Observable<any> {
    const headers = this.getHeaders();
    return this.http.get<any>(`${this.apiUrl}/${productId}`, { headers });
  }

  getMostFrequetlyProducts(count: number): Observable<any> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/frequent/${count}`;
    return this.http.get<any>(url, { headers });
  }

  getMostRecentlyProducts(count: number): Observable<any> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/recent/${count}`;
    return this.http.get<any>(url, { headers });
  }

  getTopProfitableProducts(count: number): Observable<any> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/profit/${count}`;
    return this.http.get<any>(url, { headers });
  }

  getTopPopularProducts(count: number): Observable<any> {
    const headers = this.getHeaders();
    const url = `${this.apiUrl}/popular/${count}`;
    return this.http.get<any>(url, { headers });
  }

  addProduct(newProduct: any): Observable<string> {
    const headers = this.getHeaders();
    return this.http.post<string>(`${this.apiUrl}`, {}, { headers, responseType: 'text' as 'json' });
  }

  private getHeaders(): HttpHeaders {
    const authToken = localStorage.getItem('authToken');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${authToken}`
    });
  }
}
