[English](README.md) | [Русский](README-ru.md)

# ExploreWithMe

La aplicación te ayuda a encontrar compañía para ir al cine, a un concierto, una exposición u otro evento.

## Stack tecnológico
`Java 11` `Spring Boot` `Spring Data` `Hibernate` `PostgreSQL` `Docker` `JUnit` `Mockito` `Lombok` `Maven`

## Funcionalidad

### API de usuario
- Crear, ver y editar nuevos eventos
- Enviar una solicitud de participación para participar en un evento
- Aprobar y rechazar solicitudes de participación para sus eventos
- Publicar y eliminar comentarios sobre eventos
- Editar sus comentarios dentro de las doce horas posteriores a la publicación

### API de administración
- Agregar, cambiar y eliminar categorías de eventos
- Agregar y eliminar compilaciones de eventos
- Anclar y desanclar compilaciones de eventos en la página principal
- Publicar o cancelar eventos
- Agregar, ver y eliminar usuarios
- Eliminar comentarios de otros usuarios

## Instalación

### Requisitos
- Docker Compose
- Maven
- JDK 11

### Instrucción
Para instalar la aplicación, ejecute los siguientes comandos desde la raíz de este repositorio:
- `mvn instal`
- `docker compose up`