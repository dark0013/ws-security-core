# WS Security Core

## Descripción

WS Security Core es un servicio REST desarrollado con Spring Boot orientado a la gestión de usuarios y seguridad básica de aplicaciones empresariales.

El proyecto implementa operaciones CRUD de usuarios, control de estados de cuenta, restauración lógica de usuarios y estructura preparada para futuras integraciones con autenticación JWT, control de roles y autorización.

Actualmente el sistema permite:

- Registro de usuarios
- Actualización de información
- Consulta individual y masiva
- Eliminación lógica de usuarios
- Restauración de usuarios eliminados
- Manejo de estados de usuario
- Validaciones con Jakarta Validation

---

# Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Validation
- Lombok
- Maven
- JPA / Hibernate
- PostgreSQL / MySQL (según configuración)

---

# Arquitectura

El proyecto sigue una estructura desacoplada basada en capas:

``` id="9tpdgc"
controller
service
repository
dto
entity
mapper
exception
