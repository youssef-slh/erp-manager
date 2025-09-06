import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { OrganizationService } from '@app/services/organization.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-organization-registration',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './organization-registration.html',
  styleUrls: ['./organization-registration.scss'],
})
export class OrganizationRegistration implements OnInit {
  registrationForm = this.fb.group({
    companyName: ['', Validators.required],
    domain: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phoneNumber: ['', Validators.required],
    numberOfEmpRange: ['', Validators.required],
  });

  constructor(private fb: FormBuilder, private organizationService: OrganizationService, private router: Router) { }

  ngOnInit(): void { }

  onSubmit() {
    if (this.registrationForm.valid) {
      this.organizationService.registerOrganization(this.registrationForm.value as any).subscribe(
        (response: any) => {
          console.log('Organization registered successfully', response);
          alert('Organization registered successfully!');
          this.router.navigate(['/organizations']);
        },
        (error: any) => {
          console.error('Error registering organization', error);
          alert('Error registering organization.');
        }
      );
    } else {
      alert('Please fill out all required fields correctly.');
    }
  }
}
