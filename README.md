Zeppelin OIDC
===========

This project contains maven dependencies to add libraries to zeppelin for supporting OIDC

To download dependencies :
```
mvn dependency:copy-dependencies   
```

This will copy into `target/dependecy` all jars required by Zeppelin AND by OIDC

Then you can replace all libraries in `${ZEPPELIN_HOME}/lib`
