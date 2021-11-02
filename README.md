![](https://github.com/Ferlab-Ste-Justine/zeppelin-oidc/actions/workflows/build_and_test.yml/badge.svg)
![](https://github.com/Ferlab-Ste-Justine/zeppelin-oidc/actions/workflows/release.yml/badge.svg?branch=release)

Zeppelin OIDC
===========

This project contains maven dependencies to add libraries to zeppelin for supporting OIDC

To download dependencies :
```
mvn dependency:copy-dependencies   
```

This will copy into `target/dependecy` all jars required by Zeppelin AND by OIDC

Then you can replace all libraries in `${ZEPPELIN_HOME}/lib`

## shiro.ini custom setup

### Using bio.ferlab.pac4j.UsernameAuthorizer :

In order to restrict the Zeppelin access only to certain Keycloak usernames, use this authorizer on your *shiro.ini* file as follows:

```
useridAuthorizer = bio.ferlab.pac4j.UseridAuthorizer
useridAuthorizer.elements = id1,id2,id3

config = org.pac4j.core.config.Config
config.authorizers = id:$useridAuthorizer

oidcSecurityFilter = io.buji.pac4j.filter.SecurityFilter
oidcSecurityFilter.config = $config
oidcSecurityFilter.clients = oidcClient
oidcSecurityFilter.authorizers = +id
```

Only users mentioned on the *elements* property - *id1, id2 and id3*, for instance - will be able to access the system.

### Using bio.ferlab.pac4j.authorization.generator.KeycloakRolesAuthorizationGenerator :

In theory, we should use a KeycloakOidcClient client to use keycloak as oidc provider. And roles would be directly mapped to user profile roles. 
But KeycloakOidcClient cause issues with ajax and callabackRedirect in Zeppelin. So we choose to provide an implementation of KeycloakRolesAuthorizationGenerator that can be used to map roles from keycloak.
In order to restrict the Zeppelin access only to user with certain roles, use this authorizer generator on your *shiro.ini* file as follows:

```
authorizationGenerator = bio.ferlab.pac4j.authorization.generator.KeycloakRolesAuthorizationGenerator
authorizationGenerator.clientId = zeppelin

clients = org.pac4j.core.client.Clients
clients.callbackUrl = http://localhost:8080/api/callback
clients.clients = $oidcClient
clients.ajaxRequestResolver = $ajaxRequestResolver
clients.authorizationGenerators = $authorizationGenerator
```

And then, we can use a role authorizer like this :

```
roleAuthorizer = org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
roleAuthorizer.elements = role1,role2.role3

config = org.pac4j.core.config.Config
config.clients = $clients
config.authorizers = role:$roleAuthorizer
```

Only users with roles - *role1, role2 and role3*, for instance - will be able to access the system.

Don't forget also to ask for `roles` scope in oidc client configuration :

```
oidcConfig = org.pac4j.oidc.config.OidcConfiguration
oidcConfig.scope = openid profile email roles
```


### Using bio.ferlab.pac4j.ForceDefaultURLCallbackLogic :

In order to provide Keycloak a callback URL which will override the Zeppelin predefinition, after a successful login, modify your *shiro.ini* file as follows:

```
customCallbackLogic = bio.ferlab.pac4j.ForceDefaultURLCallbackLogic

callbackFilter = io.buji.pac4j.filter.CallbackFilter
callbackFilter.defaultUrl = https://zeppelin-callback-url
callbackFilter.config = $config
callbackFilter.callbackLogic = $customCallbackLogic
```

In this case, the user will be redirected to *https://zeppelin-callback-url* after a successful Keycloak login.

## To Release
```
git tag v?.?.? 
git push origin v?.?.?
```