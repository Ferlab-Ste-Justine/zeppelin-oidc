![](https://github.com/Ferlab-Ste-Justine/zeppelin-oidc/actions/workflows/main.yml/badge.svg)

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
usernameAuthorizer = bio.ferlab.pac4j.UsernameAuthorizer
usernameAuthorizer.elements = username1,username2,username3

config = org.pac4j.core.config.Config
config.authorizers = username:$usernameAuthorizer

oidcSecurityFilter = io.buji.pac4j.filter.SecurityFilter
oidcSecurityFilter.config = $config
oidcSecurityFilter.clients = oidcClient
oidcSecurityFilter.authorizers = +username
```

Only usernames mentioned on the *elements* property - *username1, username2 and username3*, for instance - will be able to access the system.

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
