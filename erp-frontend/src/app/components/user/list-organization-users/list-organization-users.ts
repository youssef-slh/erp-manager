import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { UserService, User } from '@app/services/user.service';
import { OrganizationService, Organization } from '@app/services/organization.service';

@Component({
  selector: 'app-list-organization-users',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './list-organization-users.html',
  styleUrls: ['./list-organization-users.scss'],
})
export class ListOrganizationUsers implements OnInit {
  organizationId!: number;
  organization: Organization | undefined;
  users: User[] = [];

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private organizationService: OrganizationService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.organizationId = Number(params.get('organizationId'));
      if (this.organizationId) {
        this.loadOrganizationDetails();
        this.loadOrganizationUsers();
      }
    });
  }

  loadOrganizationDetails(): void {
    this.organizationService.getOrganizationById(this.organizationId).subscribe(
      (data: Organization) => {
        this.organization = data;
      },
      (error: any) => {
        console.error('Error fetching organization details', error);
      }
    );
  }

  loadOrganizationUsers(): void {
    this.userService.getUsersByOrganization(this.organizationId).subscribe(
      (data: User[]) => {
        this.users = data;
      },
      (error: any) => {
        console.error('Error fetching organization users', error);
      }
    );
  }
}
