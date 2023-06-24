# Eventus changelog

### Version 1.3.1 (main)
- Data visualization fixes

### Version 1.3.0
- Better error management
- Better Javadoc

### Version 1.2.1
- Fixed pom.xml output names
- Now application-*.yml and other patterns are not included in the JAR (see service/pom.xml)

### Version 1.2.0
- Complete pom.xml refactoring
- Updated to Spring Boot 3.1.0
- Updated Dockerfile
- Updated dependencies
- Switched to Postgres in develop

### Version 1.1.1
- Fixed CHANGELOG.md, README.md

### Version 1.1.0
- Removed MapStruct, replaced with custom implementation
- Added better exception handling
- Added an exception-handling case test
- More tests, more code coverage
- Past events that are unapproved now are removed every 6 hours
- After a password change, now users are logged out
- Event managers can unsubscribe users to events

### Version 1.0.0
- First release