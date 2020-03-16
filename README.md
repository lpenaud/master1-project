# master1-project

## Create database

To create database compile and lauch [bloom/src/base/Base.java](bloom/src/base/Base.java).

## Config

You can put your environnement var in `src/config/config.properties`, others name must be specify in the code.

* `url` : JDBC URL
* `user` : Username to use to login the database
* `password` : Password of database user
* `bucket` : File upload directory
* `secret` : Secret of Token authentication (by default `secret`)
* `ttl` : Time To Live for token authentication in milliseconds (by default `3600000`)

## Dependencies

Dependencies has to be in [WebContent/WEB-INF/lib](WebContent/WEB-INF/lib).

* `mysql-connector-java-8.0.19` : MySQL connector
* `jackson-annotations-2.10.2.jar` : Jackson annotations (JSON conversion)
* `jackson-core-2.10.2.jar` : Jackson core (JSON conversion)
* `jackson-databind-2.10.2.jar` : Jackson databind (JSON conversion)
