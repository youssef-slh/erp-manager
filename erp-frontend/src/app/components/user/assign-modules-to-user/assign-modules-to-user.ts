import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { UserService, User, Module } from '@app/services/user.service';
import { ModuleService} from '@app/services/module.service';

@Component({
  selector: 'app-assign-modules-to-user',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule], // ✅ Fix here
  templateUrl: './assign-modules-to-user.html',
  styleUrls: ['./assign-modules-to-user.scss'],
})
export class AssignModulesToUser implements OnInit {
  organizationId!: number;
  keycloakUserId!: string;
  user: User | undefined;
  availableModules: Module[] = [];
  userModules: Module[] = [];

  assignmentForm = this.fb.group({
    moduleIds: [[] as number[], Validators.required],
  });

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private moduleService: ModuleService
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.organizationId = Number(params.get('organizationId'));
      this.keycloakUserId = params.get('keycloakUserId') || '';

      if (this.organizationId && this.keycloakUserId) {
        this.loadUserDetails();
        this.loadAvailableModules();
        this.loadUserModules();
      }
    });
  }

  loadUserDetails(): void {
    // Assuming a method to get user details by keycloakUserId and organizationId
    // This will likely need to be added to UserService and backend UserController/Service
    // For now, we'll just set a placeholder or fetch modules.
    // A real implementation would fetch user details and populate 'user' object.
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

  loadUserModules(): void {
    this.userService.getUserModules(this.keycloakUserId, this.organizationId).subscribe(
      (data: Module[]) => {
        this.userModules = data;
        const userModuleIds = data.map((module: Module) => module.id);
        this.assignmentForm.get('moduleIds')?.setValue(userModuleIds);
      },
      (error: any) => {
        console.error('Error fetching user modules', error);
      }
    );
  }

  onModuleChange(event: any, moduleId: number): void {
    const moduleIds = this.assignmentForm.get('moduleIds')?.value || [];
    if (event.target.checked) {
      moduleIds.push(moduleId);
    } else {
      const index = moduleIds.indexOf(moduleId);
      if (index > -1) {
        moduleIds.splice(index, 1);
      }
    }
    this.assignmentForm.get('moduleIds')?.setValue(moduleIds);
  }

  isModuleAssigned(moduleId: number): boolean {
    return this.userModules.some((module: Module) => module.id === moduleId);
  }

  onSubmit(): void {
    if (this.assignmentForm.valid) {
      const moduleIdsToAssign = this.assignmentForm.value.moduleIds || [];
      const assignmentRequest = { moduleIds: moduleIdsToAssign };

      this.userService.assignModulesToUser(this.keycloakUserId, this.organizationId, assignmentRequest).subscribe(
        (response: any) => {
          console.log('Modules assigned successfully', response);
          alert('Modules assigned successfully!');
          this.router.navigate(['/organizations', this.organizationId, 'users']); // Navigate back to user list
        },
        (error: any) => {
          console.error('Error assigning modules', error);
          alert('Error assigning modules.');
        }
      );
    } else {
      alert('Please select at least one module.');
    }
  }
}
