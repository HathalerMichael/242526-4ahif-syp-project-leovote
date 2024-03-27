import {KeycloakService} from "keycloak-angular";
import Keycloak from "keycloak-js";

export function initializeKeycloak(keycloak: KeycloakService): () => Promise<boolean> {
  return () =>
    keycloak.init({
      config: {
        url: 'https://auth.htl-leonding.ac.at',
        realm: 'htl-leonding',
        clientId: 'leovotetest',
      },
      initOptions: {
        checkLoginIframe: true,
        checkLoginIframeInterval: 20
      },
      loadUserProfileAtStartUp: true
    });
}
