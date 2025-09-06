import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

interface OrganizationRegistrationRequest {
  companyName: string;
  domain: string;
  email: string;
  phoneNumber: string;
  numberOfEmpRange: string;
}

export interface Organization {
  id: number;
  companyName: string;
  domain: string;
  icon: string;
  description: string;
  email: string;
  phoneNumber: string;
  numberOfEmpRange: string;
}

@Injectable({
  providedIn: 'root'
})
export class OrganizationService {

  private apiUrl = 'http://localhost:8085/api/v1/organizations'; // Adjust as per your backend API gateway

  constructor(private http: HttpClient) { }

  registerOrganization(request: OrganizationRegistrationRequest): Observable<any> {
    return this.http.post<any>(this.apiUrl, request);
  }

  getAllOrganizations(): Observable<Organization[]> {
    return this.http.get<Organization[]>(this.apiUrl);
  }

  getOrganizationById(id: number): Observable<Organization> {
    return this.http.get<Organization>(`${this.apiUrl}/${id}`);
  }
}
