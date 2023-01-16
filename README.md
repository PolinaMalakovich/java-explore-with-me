# ExploreWithMe

ExploreWithMe helps you find events to go to and people to go with.

## Stack
`Java 11` `Spring Boot` `Spring Data` `Hibernate` `PostgreSQL` `Docker` `JUnit` `Mockito` `Lombok` `Maven`

## Functionality

### User API
- Create new events, edit and view them
- Send a participation request to take part in an event
- Approve and reject participation requests for your events
- Post and delete comments on events 
- Edit your comments within twelve hours of posting

### Admin API
- Add, change and delete event categories
- Add and delete event compilations
- Pin and unpin event compilations on the main page
- Publish or cancel events
- Add, view and delete users
- Delete comments from other users

## Instalation

### Requirements
- Docker Compose
- Maven
- JDK 11

### Instruction
To instal the app run the following commands from the root of this repository:
- `mvn instal`
- `docker compose up`
