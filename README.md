# EVENTUS - Event management system

## Getting started

To run this app locally you need:
- MySQL server
- Java IDE
- .env file based on .env.example

To run tests MySQL is not needed

## Getting stared: deploy profile

Required things:
- ```application-deploy.yml``` to inject into ```/opt/app/config```
- A working MySQL database with all the required tables

### Generate Javadoc

```mvn javadoc:javadoc```