import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Module {
  id: number;
  name: string;
  icon: string;
  description: string;
  active: boolean;
  currency: string;
  price: number;
}

interface ModuleSubscriptionRequest {
  organizationId: number;
  moduleId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ModuleService {

  private apiUrl = 'http://localhost:8085/v1/modules'; // Adjust as per your backend API gateway

  constructor(private http: HttpClient) { }

  getAllModules(): Observable<Module[]> {
    return this.http.get<Module[]>(this.apiUrl);
  }

  getSubscribedModules(organizationId: number): Observable<Module[]> {
    return this.http.get<Module[]>(`${this.apiUrl}/organization/${organizationId}`);
  }

  subscribeModuleToOrganization(request: ModuleSubscriptionRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/subscribe`, request);
  }
}
