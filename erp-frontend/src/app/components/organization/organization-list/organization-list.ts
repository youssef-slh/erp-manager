import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OrganizationService, Organization } from '@app/services/organization.service';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-organization-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './organization-list.html',
  styleUrls: ['./organization-list.scss'],
})
export class OrganizationList implements OnInit {
  organizations: Organization[] = [];

  constructor(private organizationService: OrganizationService) { }

  ngOnInit(): void {
    this.loadOrganizations();
  }

  loadOrganizations(): void {
    this.organizationService.getAllOrganizations().subscribe(
      (data: Organization[]) => {
        this.organizations = data;
      },
      (error: any) => {
        console.error('Error fetching organizations', error);
      }
    );
  }
}
