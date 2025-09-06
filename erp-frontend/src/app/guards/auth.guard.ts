import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { KeycloakService } from 'keycloak-angular';

export const authGuard: CanActivateFn = (route, state) => {
  const keycloakService = inject(KeycloakService);
  const router = inject(Router);

  return new Promise(async (resolve, reject) => {
    try {
      const authenticated = await keycloakService.isLoggedIn();
      const requiredRoles = route.data['roles'];

      if (!authenticated) {
        await keycloakService.login();
        return;
      }

      if (!requiredRoles || requiredRoles.length === 0) {
        return resolve(true);
      }

      const userRoles = keycloakService.getUserRoles();
      const hasRequiredRole = requiredRoles.some((role: string) => userRoles.includes(role));

      if (hasRequiredRole) {
        resolve(true);
      } else {
        alert('Access denied'); // TODO: Implement a proper unauthorized page/handling
        router.navigate(['/']);
        resolve(false);
      }
    } catch (error) {
      reject('Error in authentication guard');
    }
  });
};
