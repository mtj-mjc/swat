import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Group } from '../models/group';
import { Store } from '../models/store';
import { User } from '../models/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  baseUrl = environment.baseUrl;

  constructor(private httpClient: HttpClient) {}

  createUser(user: User): Observable<any> {
    const url = this.baseUrl + '/api/v1/user/';
    return this.httpClient.put(url, user);
  }

  getAllGroups(): Observable<Group[]> {
    const url = this.baseUrl + `/api/v1/group/`;
    return this.httpClient.get<Group[]>(url);
  }

  getAllStores(): Observable<Store[]> {
    const url = this.baseUrl + `/api/v1/store/`;
    return this.httpClient.get<Store[]>(url);
  }
}
