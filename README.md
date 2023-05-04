# EVENTUS - Event management system

## Getting started

To run this app locally you need:
- MySQL server
- Java IDE
- .env file based on .env.example

To run tests MySQL is not needed

## Getting stared: deploy profile

Required files:
- ```application-deploy.yml``` to inject into ```/opt/app/config```
- ```schema.sql``` to inject in the database, a default ```schema-mysql.sql``` can be found inside API resources

### Generate Javadoc

```mvn javadoc:javadoc```