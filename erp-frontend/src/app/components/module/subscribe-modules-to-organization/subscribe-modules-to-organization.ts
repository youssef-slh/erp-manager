import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { OrganizationService, Organization } from '@app/services/organization.service';
import { ModuleService, Module } from '@app/services/module.service';

@Component({
  selector: 'app-subscribe-modules-to-organization',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './subscribe-modules-to-organization.html',
  styleUrls: ['./subscribe-modules-to-organization.scss'],
})
export class SubscribeModulesToOrganization implements OnInit {
  organizationId!: number;
  organization: Organization | undefined;
  availableModules: Module[] = [];
  subscribedModules: Module[] = [];

  subscriptionForm = this.fb.group({
    moduleIds: [[] as number[], Validators.required],
  });

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private organizationService: OrganizationService,
    private moduleService: ModuleService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.organizationId = Number(params.get('organizationId'));
      if (this.organizationId) {
        this.loadOrganizationDetails();
        this.loadAvailableModules();
        this.loadSubscribedModules();
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

  loadAvailableModules(): void {
    this.moduleService.getAllModules().subscribe(
      (data: Module[]) => {
        this.availableModules = data;
      },
      (error: any) => {
        console.error('Error fetching available modules', error);
      }
    );
  }

  loadSubscribedModules(): void {
    this.moduleService.getSubscribedModules(this.organizationId).subscribe(
      (data: Module[]) => {
        this.subscribedModules = data;
        const subscribedModuleIds = data.map((module: Module) => module.id);
        this.subscriptionForm.get('moduleIds')?.setValue(subscribedModuleIds);
      },
      (error: any) => {
        console.error('Error fetching subscribed modules', error);
      }
    );
  }

  onModuleChange(event: any, moduleId: number): void {
    const moduleIds = this.subscriptionForm.get('moduleIds')?.value || [];
    if (event.target.checked) {
      moduleIds.push(moduleId);
    } else {
      const index = moduleIds.indexOf(moduleId);
      if (index > -1) {
        moduleIds.splice(index, 1);
      }
    }
    this.subscriptionForm.get('moduleIds')?.setValue(moduleIds);
  }

  isModuleSubscribed(moduleId: number): boolean {
    return this.subscribedModules.some((module: Module) => module.id === moduleId);
  }

  onSubmit(): void {
    if (this.subscriptionForm.valid) {
      const moduleIdsToSubscribe = this.subscriptionForm.value.moduleIds || [];
      // For simplicity, we'll re-subscribe all selected modules.
      // A more robust solution would calculate diffs (additions/removals).
      moduleIdsToSubscribe.forEach((moduleId: number) => {
        this.moduleService.subscribeModuleToOrganization({ organizationId: this.organizationId, moduleId }).subscribe(
          (response: any) => {
            console.log('Module subscribed successfully', response);
          },
          (error: any) => {
            console.error('Error subscribing module', error);
          }
        );
      });
      alert('Modules updated successfully!');
      this.router.navigate(['/organizations']); // Navigate back to organization list
    } else {
      alert('Please select at least one module.');
    }
  }
}
