package com.dev2ever;

import io.quarkus.oidc.AccessTokenCredential;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filter to automatically add the Bearer token to outgoing REST client requests.
 * This propagates the user's token to backend services.
 */
@Provider
public class OidcClientRequestFilter implements ClientRequestFilter {

    @Inject
    AccessTokenCredential accessToken;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        // Add the bearer token to the request
        if (accessToken != null && accessToken.getToken() != null) {
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken());
        }
    }
}
