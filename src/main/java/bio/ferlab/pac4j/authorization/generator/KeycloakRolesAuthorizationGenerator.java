package bio.ferlab.pac4j.authorization.generator;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.SignedJWT;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.oidc.profile.OidcProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * This class is a copy from {@link org.pac4j.oidc.authorization.generator.KeycloakRolesAuthorizationGenerator}, but more generic.
 * Instead of accepting only KeycloakProfile, it works with any instance of OidcProfile.
 *
 */
public class KeycloakRolesAuthorizationGenerator implements AuthorizationGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeycloakRolesAuthorizationGenerator.class);

    private String clientId;

    public KeycloakRolesAuthorizationGenerator() {}

    public KeycloakRolesAuthorizationGenerator(final String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Optional<UserProfile> generate(final WebContext context, final SessionStore sessionStore, final UserProfile profile) {

        if (profile instanceof OidcProfile) { //here difference with original file
            try {
                final JWT jwt = SignedJWT.parse(((OidcProfile) profile).getAccessToken().getValue());
                final var jwtClaimsSet = jwt.getJWTClaimsSet();

                final var realmRolesJsonObject = jwtClaimsSet.getJSONObjectClaim("realm_access");
                if (realmRolesJsonObject != null) {
                    final var realmRolesJsonArray = (JSONArray) realmRolesJsonObject.get("roles");
                    if (realmRolesJsonArray != null) {
                        realmRolesJsonArray.forEach(role -> profile.addRole((String) role));
                    }
                }

                if (clientId != null) {
                    final var resourceAccess = jwtClaimsSet.getJSONObjectClaim("resource_access");
                    if (resourceAccess != null) {
                        final var clientRolesJsonObject = (JSONObject) resourceAccess.get(clientId);
                        if (clientRolesJsonObject != null) {
                            final var clientRolesJsonArray = (JSONArray) clientRolesJsonObject.get("roles");
                            if (clientRolesJsonArray != null) {
                                clientRolesJsonArray.forEach(role -> profile.addRole((String) role));
                            }
                        }
                    }
                }
            } catch (final Exception e) {
                LOGGER.warn("Cannot parse Keycloak roles", e);
            }
        }

        return Optional.of(profile);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }
}
