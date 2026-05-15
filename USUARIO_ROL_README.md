# CRUD UsuarioRol

## Descripción
CRUD completo para la gestión de las asignaciones de usuarios con roles. La entidad UsuarioRol permite establecer relaciones Many-to-One con las entidades User y Rol.

## Estructura

### 1. Entidad UsuarioRol
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/entity/UsuarioRol.java`

Campos:
- `id` (Long): Identificador único
- `usuario` (User): Relación ManyToOne con la entidad User
- `rol` (Rol): Relación ManyToOne con la entidad Rol
- `estado` (Boolean): Estado activo/inactivo (por defecto true)
- `usuarioCreacion` (String): Usuario que realizó la creación (por defecto "admin")
- `fechaCreacion` (LocalDateTime): Fecha de creación (se asigna automáticamente)
- `usuarioActualizacion` (String): Usuario que realizó la última actualización (por defecto "admin")
- `fechaActualizacion` (LocalDateTime): Fecha de la última actualización (se actualiza automáticamente)

### 2. Repositorio
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/repository/UsuarioRolRepository.java`

Métodos:
- `findByUsuarioAndRol()`: Buscar asignación por usuario y rol
- `findByUsuario()`: Obtener todos los roles de un usuario
- `findByRol()`: Obtener todos los usuarios de un rol
- `existsByUsuarioAndRol()`: Validar si existe la asignación

### 3. DTOs

**UsuarioRolRequestDto**
- `usuarioId` (Long): ID del usuario (obligatorio)
- `rolId` (Long): ID del rol (obligatorio)
- `estado` (Boolean): Estado de la asignación (opcional, por defecto true)

**UsuarioRolResponseDto**
- `id` (Long): Identificador de la asignación
- `usuarioId` (Long): ID del usuario
- `usuarioUsername` (String): Username del usuario
- `rolId` (Long): ID del rol
- `rolNombre` (String): Nombre del rol
- `estado` (Boolean): Estado de la asignación
- `usuarioCreacion` (String): Usuario de creación
- `fechaCreacion` (LocalDateTime): Fecha de creación
- `usuarioActualizacion` (String): Usuario de actualización
- `fechaActualizacion` (LocalDateTime): Fecha de actualización

### 4. Mapper
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/mapper/UsuarioRolMapper.java`

Utiliza MapStruct para mapear entre entidad y DTOs, incluyendo mappings de relaciones.

### 5. Servicio
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/service/UsuarioRolService.java` (interfaz)
**Implementación:** `src/main/java/com/darkross/wssecuritycore/service/impl/UsuarioRolServiceImpl.java`

Métodos Públicos:
- `createUsuarioRol()`: Crear nueva asignación 
  - ✅ Valida que el usuario exista
  - ✅ Valida que el rol exista
  - ✅ Valida que no exista duplicada
- `updateUsuarioRol()`: Actualizar asignación
  - ✅ Valida que el usuario exista
  - ✅ Valida que el rol exista
  - ✅ Valida que no exista duplicada
- `getUsuarioRolById()`: Obtener por ID
- `getAllUsuarioRoles()`: Obtener todas las asignaciones
- `getUsuarioRolesByIdUsuario()`: Obtener por usuario (valida existencia)
- `getUsuarioRolesByIdRol()`: Obtener por rol (valida existencia)
- `deleteUsuarioRol()`: Eliminar lógicamente (estado = false)
- `restoreUsuarioRol()`: Restaurar asignación

Métodos Auxiliares Privados:
- `validateAndGetUsuario()`: Valida y obtiene el usuario de la base de datos
  - Lanza `UserNotFoundException` si no existe
- `validateAndGetRol()`: Valida y obtiene el rol de la base de datos
  - Lanza `RolNotFoundException` si no existe

### 6. Controlador
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/controller/UsuarioRolController.java`

Endpoint base: `/api/v1/usuario-roles`

#### Endpoints

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/` | Crear nueva asignación |
| PUT | `/{id}` | Actualizar asignación |
| GET | `/{id}` | Obtener por ID |
| GET | `/` | Obtener todas |
| GET | `/usuario/{idUsuario}` | Obtener por usuario |
| GET | `/rol/{idRol}` | Obtener por rol |
| DELETE | `/{id}` | Eliminar (estado = false) |
| PATCH | `/{id}/restore` | Restaurar (estado = true) |

### 7. Excepciones Personalizadas
- `UsuarioRolNotFoundException`: Se lanza cuando no se encuentra la asignación
- `UsuarioRolDuplicatedException`: Se lanza cuando ya existe la asignación usuario-rol

## Ejemplos de Uso

### Crear una asignación usuario-rol
```bash
curl -X POST http://localhost:8080/api/v1/usuario-roles \
  -H "Content-Type: application/json" \
  -d '{
    "usuarioId": 1,
    "rolId": 1,
    "estado": true
  }'
```

### Obtener todas las asignaciones
```bash
curl -X GET http://localhost:8080/api/v1/usuario-roles
```

### Obtener roles de un usuario
```bash
curl -X GET http://localhost:8080/api/v1/usuario-roles/usuario/1
```

### Obtener usuarios de un rol
```bash
curl -X GET http://localhost:8080/api/v1/usuario-roles/rol/1
```

### Eliminar una asignación
```bash
curl -X DELETE http://localhost:8080/api/v1/usuario-roles/1
```

### Restaurar una asignación
```bash
curl -X PATCH http://localhost:8080/api/v1/usuario-roles/1/restore
```

## Base de Datos

Se debe ejecutar el script SQL ubicado en `scripts/create_usuario_rol_table.sql` para crear la tabla:

```sql
CREATE TABLE IF NOT EXISTS usuario_rol (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_usuario BIGINT NOT NULL,
    id_rol BIGINT NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    usuario_creacion VARCHAR(50) NOT NULL DEFAULT 'admin',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT 'admin',
    fecha_actualizacion DATETIME ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol_usuario FOREIGN KEY (id_usuario) REFERENCES users(id),
    CONSTRAINT fk_usuario_rol_rol FOREIGN KEY (id_rol) REFERENCES roles(id),
    CONSTRAINT uk_usuario_rol UNIQUE KEY (id_usuario, id_rol)
);
```

## Validaciones Implementadas

### Validación al Crear UsuarioRol
1. **Validación de Usuario**: Verifica que el usuario ID proporcionado exista en la BD
   - Lanza `UserNotFoundException (404)` si no existe
2. **Validación de Rol**: Verifica que el rol ID proporcionado exista en la BD
   - Lanza `RolNotFoundException (404)` si no existe
3. **Validación de Duplicado**: Verifica que no exista ya una asignación usuario-rol
   - Lanza `UsuarioRolDuplicatedException (409)` si ya existe

### Validación al Actualizar UsuarioRol
1. **Validación de UsuarioRol**: Verifica que el registro a actualizar exista
   - Lanza `UsuarioRolNotFoundException (404)` si no existe
2. **Validación de Usuario**: Verifica que el usuario ID proporcionado exista en la BD
   - Lanza `UserNotFoundException (404)` si no existe
3. **Validación de Rol**: Verifica que el rol ID proporcionado exista en la BD
   - Lanza `RolNotFoundException (404)` si no existe
4. **Validación de Duplicado**: Si se cambian usuario o rol, verifica que no exista esa combinación
   - Lanza `UsuarioRolDuplicatedException (409)` si ya existe

### Validación al Obtener Roles de un Usuario
- Verifica que el usuario ID exista antes de buscar sus roles
- Lanza `UserNotFoundException (404)` si el usuario no existe

### Validación al Obtener Usuarios de un Rol
- Verifica que el rol ID exista antes de buscar sus usuarios
- Lanza `RolNotFoundException (404)` si el rol no existe

## Características

✅ Relaciones bidireccionales con User y Rol
✅ Auditoría automática (usuario y fecha de creación/actualización)
✅ Validación de duplicados
✅ Soft delete (estado = false)
✅ Manejo robusto de excepciones
✅ Transacciones gestionadas automáticamente
✅ Mapeo automatizado con MapStruct
✅ Índices en base de datos para optimizar búsquedas

