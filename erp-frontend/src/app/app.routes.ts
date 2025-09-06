import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { OrganizationRegistration } from './components/organization/organization-registration/organization-registration';
import { OrganizationList } from './components/organization/organization-list/organization-list';
import { ListAvailableModules } from './components/module/list-available-modules/list-available-modules';
import { SubscribeModulesToOrganization } from './components/module/subscribe-modules-to-organization/subscribe-modules-to-organization';
import { AddUserToOrganization } from './components/user/add-user-to-organization/add-user-to-organization';
import { ListOrganizationUsers } from './components/user/list-organization-users/list-organization-users';
import { AssignModulesToUser } from './components/user/assign-modules-to-user/assign-modules-to-user';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component: Home, canActivate: [authGuard] },
    { 
        path: 'organizations', 
        component: OrganizationList, 
        canActivate: [authGuard], 
        data: { roles: ['ADMIN_PUBLIC'] }
    },
    { 
        path: 'organizations/register', 
        component: OrganizationRegistration, 
        canActivate: [authGuard], 
        data: { roles: ['ADMIN_PUBLIC'] }
    },
    {
        path: 'modules/available',
        component: ListAvailableModules,
        canActivate: [authGuard],
        data: { roles: ['ADMIN_PUBLIC'] }
    },
    {
        path: 'organizations/:organizationId/modules/subscribe',
        component: SubscribeModulesToOrganization,
        canActivate: [authGuard],
        data: { roles: ['ADMIN_PUBLIC'] }
    },
    {
        path: 'organizations/:organizationId/users/add',
        component: AddUserToOrganization,
        canActivate: [authGuard],
        data: { roles: ['ORG_ADMIN'] } // Assuming ORG_ADMIN can add users
    },
    {
        path: 'organizations/:organizationId/users',
        component: ListOrganizationUsers,
        canActivate: [authGuard],
        data: { roles: ['ORG_ADMIN', 'ADMIN_PUBLIC'] } // Both can view users
    },
    {
        path: 'organizations/:organizationId/users/:keycloakUserId/assign-modules',
        component: AssignModulesToUser,
        canActivate: [authGuard],
        data: { roles: ['ORG_ADMIN'] } // Only ORG_ADMIN can assign modules
    }
];
