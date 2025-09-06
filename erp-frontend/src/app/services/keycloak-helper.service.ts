import { Injectable, PLATFORM_ID, Inject } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import type KeycloakType from 'keycloak-js';
import { KeycloakLoginOptions } from 'keycloak-js';

@Injectable({
  providedIn: 'root'
})
export class KeycloakService {
  private keycloakInstance: KeycloakType | undefined;
  private isBrowser: boolean;
  private authenticated = false;

  // Hardcoded Keycloak configuration
  private readonly keycloakConfig = {
    url: 'https://localapi.digitalams.net/auth',
    realm: 'erp-manager-dev',
    clientId: 'onboarding_manager'
  };

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    this.isBrowser = isPlatformBrowser(platformId);
    console.log('KeycloakService running in browser?', this.isBrowser);
  }

  /**
   * Initializes Keycloak with login-required
   */
  async init(): Promise<boolean> {
    if (!this.isBrowser) {
      console.warn('KeycloakService: Not running in a browser. Skipping Keycloak init.');
      return false;
    }

    // Lazy load keycloak-js only in the browser
    const { default: Keycloak } = await import('keycloak-js');

    this.keycloakInstance = new Keycloak({
      url: this.keycloakConfig.url,
      realm: this.keycloakConfig.realm,
      clientId: this.keycloakConfig.clientId
    });

    try {
      const authenticated = await this.keycloakInstance.init({
        checkLoginIframe: false,
        onLoad: 'login-required',
        enableLogging: true,
        pkceMethod: false,
        flow: 'standard'
      });
      this.authenticated = authenticated;
      console.log(`✅ Keycloak initialized. Authenticated: ${authenticated}`);
      return authenticated;
    } catch (error) {
      console.error('❌ Keycloak initialization failed', error);
      return false;
    }
  }

  /**
   * Cached authentication status
   */
  get isAuthenticated(): boolean {
    return this.authenticated;
  }

  isLoggedIn(): boolean {
    return this.authenticated;
  }

  /**
   * Raw Keycloak instance
   */
  getKeycloakInstance(): KeycloakType | undefined {
    return this.keycloakInstance;
  }

  /**
   * Get token
   */
  getToken(): string | undefined {
    return this.keycloakInstance?.token;
  }

  /**
   * Get user roles
   */
  getRoles(): string[] {
    return this.keycloakInstance?.realmAccess?.roles || [];
  }

  /**
   * Login
   */
  login(options?: KeycloakLoginOptions): void {
    if (!this.isBrowser) return;
    this.keycloakInstance?.login(options);
  }

  // /**
  //  * Logout
  //  */
  // logout(redirectUri: string = '/'): void {
  //   if (!this.isBrowser) return;
  //   this.keycloakInstance?.logout({ redirectUri: window.location.origin || redirectUri });
  // }

  /**
   * Refresh token
   */
  async updateToken(minValidity: number = 60): Promise<boolean> {
    if (!this.keycloakInstance) return false;

    try {
      const refreshed = await this.keycloakInstance.updateToken(minValidity);
      console.log(refreshed ? '🔄 Token was refreshed' : '✅ Token is still valid');
      return true;
    } catch (err) {
      console.error('❌ Failed to refresh token', err);
      return false;
    }
  }
}
