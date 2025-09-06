import { ApplicationConfig, APP_INITIALIZER, importProvidersFrom, PLATFORM_ID, Inject } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideClientHydration } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { isPlatformBrowser } from '@angular/common';

import { routes } from './app.routes';

function initializeKeycloakFactory(keycloak: KeycloakService, platformId: Object) {
  return () => {
    if (!isPlatformBrowser(platformId)) {
      // Skip Keycloak initialization on the server
      return Promise.resolve(false);
    }

    return keycloak.init({
      config: {
        url: 'https://localapi.digitalams.net/auth',
        realm: 'erp-manager-dev',
        clientId: 'onboarding_manager',
      },
      initOptions: {
        checkLoginIframe: false,
        onLoad: 'login-required',
        enableLogging: true,
        pkceMethod: false, // disables PKCE so Crypto API won't be used
        flow: 'standard'
      },
      enableBearerInterceptor: true,
      bearerPrefix: 'Bearer',
      bearerExcludedUrls: ['/assets'],
    });
  };
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideClientHydration(),
    importProvidersFrom(HttpClientModule, KeycloakAngularModule),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloakFactory,
      multi: true,
      deps: [KeycloakService, PLATFORM_ID], // Inject PLATFORM_ID to check SSR
    },
  ],
};
