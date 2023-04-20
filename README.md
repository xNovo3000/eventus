# EVENTUS - Event management system

## Getting started

To run this app locally you need:
- MySQL server
- Java IDE
- .env file based on .env.example

To run tests MySQL is not needed

## Getting stared: deploy profile

Required environment variables:
- DATABASE_FQDN (like ```my-database.server.com```)
- DATABASE_USERNAME
- DATABASE_PASSWORD
- EMAIL_SMTP
- EMAIL_PORT
- EMAIL_USERNAME
- EMAIL_PASSWORD

### Generate Javadoc

```mvn javadoc:javadoc```