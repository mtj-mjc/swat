import { Injectable } from '@angular/core';
import { Order } from '../models/order';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  baseUrl = environment.baseUrl;

  constructor(private httpClient: HttpClient) {}

  createOrder(order: Order): Observable<any> {
    const url = this.baseUrl + '/api/v1/order/';
    return this.httpClient.put(url, order);
  }

  getAllOrders(): Observable<Order[]> {
    const url = this.baseUrl + '/api/v1/order/';
    return this.httpClient.get<Order[]>(url);
  }

  getAllOrdersFromUser(username: string): Observable<Order[]> {
    const url = this.baseUrl + `/api/v1/order/user/${username}`;
    return this.httpClient.get<Order[]>(url);
  }
}
