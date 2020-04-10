import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  baseUrl = environment.baseUrl;

  constructor(private httpClient: HttpClient) {}

  getAllProducts(): Observable<Product[]> {
    const url = this.baseUrl + '/api/v1/product/';
    return this.httpClient.get<Product[]>(url);
  }
}
