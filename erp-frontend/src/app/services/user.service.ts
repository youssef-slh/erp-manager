import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface UserRegistrationRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  organizationId: number;
}

interface UserModuleAssignmentRequest {
  moduleIds: number[];
}

export interface Module {
  id: number;
  name: string;
  icon: string;
  description: string;
  active: boolean;
  currency: string;
  price: number;
}

export interface User {
  id: number;
  keycloakUserId: string;
  organizationId: number;
  modules: Module[];
}

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8085/api/v1/users'; // Adjust as per your backend API gateway

  constructor(private http: HttpClient) { }

  registerUser(request: UserRegistrationRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/invite`, request);
  }

  assignModulesToUser(keycloakUserId: string, organizationId: number, request: UserModuleAssignmentRequest): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${keycloakUserId}/organizations/${organizationId}/modules`, request);
  }

  getUserModules(keycloakUserId: string, organizationId: number): Observable<Module[]> {
    return this.http.get<Module[]>(`${this.apiUrl}/${keycloakUserId}/organizations/${organizationId}/modules`);
  }

  getUsersByOrganization(organizationId: number): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/organization/${organizationId}`);
  }
}
