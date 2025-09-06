import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '@app/services/user.service';
import { OrganizationService, Organization } from '@app/services/organization.service';

@Component({
  selector: 'app-add-user-to-organization',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './add-user-to-organization.html',
  styleUrls: ['./add-user-to-organization.scss'],
})
export class AddUserToOrganization implements OnInit {
  organizationId!: number;
  organization: Organization | undefined;

  userRegistrationForm = this.fb.group({
    firstName: ['', Validators.required],
    lastName: ['', Validators.required],
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
  });

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private organizationService: OrganizationService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.organizationId = Number(params.get('organizationId'));
      if (this.organizationId) {
        this.loadOrganizationDetails();
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

  onSubmit(): void {
    if (this.userRegistrationForm.valid) {
      const formValue = this.userRegistrationForm.value;
      const registrationRequest = {
        firstName: formValue.firstName!,
        lastName: formValue.lastName!,
        username: formValue.username!,
        email: formValue.email!,
        organizationId: this.organizationId
      };

      this.userService.registerUser(registrationRequest).subscribe(
        (response: any) => {
          console.log('User registered and invited successfully', response);
          alert('User registered and invited successfully!');
          this.router.navigate(['/organizations', this.organizationId, 'users']); // Navigate to user list within organization
        },
        (error: any) => {
          console.error('Error registering user', error);
          alert('Error registering user.');
        }
      );
    } else {
      alert('Please fill out all required fields correctly.');
    }
  }
}
