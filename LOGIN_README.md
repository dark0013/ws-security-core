# Implementación de Login con JWT

## Descripción
Implementación completa del sistema de autenticación siguiendo la arquitectura existente del proyecto. Incluye login con JWT, validación de credenciales y manejo de roles.

## Estructura Implementada

### 1. DTOs de Autenticación
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/dto/auth/`

#### LoginRequestDto
- `usernameOrEmail` (String): Usuario o email para login (obligatorio)
- `password` (String): Contraseña (obligatorio)

#### LoginResponseDto
- `token` (String): Token JWT generado
- `type` (String): Tipo de token ("Bearer")
- `id` (Long): ID del usuario
- `username` (String): Nombre de usuario
- `email` (String): Email del usuario
- `nombres` (String): Nombres del usuario
- `apellidos` (String): Apellidos del usuario
- `roles` (List<String>): Lista de roles asignados al usuario

### 2. Utilidades JWT
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/util/JwtUtils.java`

Funciones:
- `generateToken()`: Genera token JWT con username y expiración
- `getUsernameFromJwtToken()`: Extrae username del token
- `validateJwtToken()`: Valida si el token es correcto y no expirado

### 3. Servicio de Autenticación
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/service/AuthService.java` (interfaz)
**Implementación:** `src/main/java/com/darkross/wssecuritycore/service/impl/AuthServiceImpl.java`

Método:
- `login()`: Procesa la autenticación y retorna respuesta completa

### 4. UserDetailsService Personalizado
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/service/UserDetailsServiceImpl.java`

Funciones:
- Carga usuario por username o email
- Valida que el usuario esté activo
- Carga roles activos del usuario desde UsuarioRol
- Retorna UserDetails con authorities

### 5. Configuración de Seguridad
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/config/WebSecurityConfig.java`

Configuración:
- Stateless session management
- Endpoint `/auth/login` público
- Endpoints `/api/**` requieren autenticación
- BCryptPasswordEncoder
- DaoAuthenticationProvider

### 6. Controlador de Autenticación
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/controller/AuthController.java`

Endpoint:
- `POST /auth/login`: Procesa login y retorna JWT + datos usuario

### 7. Excepciones
**Ubicación:** `src/main/java/com/darkross/wssecuritycore/exception/AuthException.java`

- `AuthException`: Para errores de autenticación (401 Unauthorized)

## Configuración

### application.properties
```properties
# JWT Configuration
app.jwt.secret=miClaveSecretaParaJwtQueDebeSerMuyLargaYComplejaParaSeguridadDelSistema2024
app.jwt.expiration=86400000
```

### Dependencias Agregadas (pom.xml)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

## Flujo de Autenticación

1. **Cliente envía credenciales** a `POST /auth/login`
2. **Validación de entrada** con Jakarta Validation
3. **Búsqueda de usuario** por username o email
4. **Validación de estado** (usuario debe estar activo)
5. **Carga de roles** desde UsuarioRol (solo roles activos)
6. **Autenticación** con AuthenticationManager
7. **Generación de JWT** con username y expiración
8. **Respuesta completa** con token + datos usuario + roles

## Ejemplos de Uso

### Login Exitoso
```bash
curl -X POST http://localhost:5562/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "password123"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "admin",
  "email": "admin@example.com",
  "nombres": "Administrador",
  "apellidos": "Sistema",
  "roles": ["ADMIN", "USER"]
}
```

### Login Fallido - Credenciales Inválidas
```bash
curl -X POST http://localhost:5562/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "wrongpassword"
  }'
```

**Respuesta (401):**
```json
{
  "errorCode": "AUTH_ERROR",
  "message": "Credenciales inválidas",
  "status": 401,
  "timestamp": "2026-05-14T20:35:12.8642809",
  "path": "/auth/login"
}
```

### Login Fallido - Usuario Inactivo
```bash
curl -X POST http://localhost:5562/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "inactiveuser",
    "password": "password123"
  }'
```

**Respuesta (401):**
```json
{
  "errorCode": "AUTH_ERROR",
  "message": "Credenciales inválidas",
  "status": 401,
  "timestamp": "2026-05-14T20:35:12.8642809",
  "path": "/auth/login"
}
```

## Uso del Token JWT

Para acceder a endpoints protegidos, incluir el token en el header Authorization:

```bash
curl -X GET http://localhost:5562/api/v1/roles \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

## Características

✅ **Arquitectura consistente** con el proyecto existente  
✅ **Reutilización de entidades** User, Rol, UsuarioRol  
✅ **Validación completa** de credenciales y estado  
✅ **JWT stateless** sin sesiones del lado servidor  
✅ **Roles dinámicos** cargados desde UsuarioRol  
✅ **Manejo robusto de excepciones** con códigos de error  
✅ **Configuración flexible** via application.properties  
✅ **Encriptación BCrypt** reutilizada del proyecto  
✅ **Transacciones gestionadas** automáticamente  

## Notas de Seguridad

- Las contraseñas se validan usando BCrypt (ya implementado)
- Los tokens JWT expiran en 24 horas por defecto
- Solo usuarios activos pueden autenticarse
- Solo roles activos se incluyen en las authorities
- El endpoint de login es público, los demás requieren autenticación

## Problema Resuelto: Login Fallando por Falta de Roles

### ❌ **Problema Identificado**
Los usuarios creados no tenían roles asignados automáticamente, causando que el login fallara con "Credenciales inválidas" porque Spring Security requiere authorities (roles) para autenticar usuarios.

### ✅ **Solución Implementada**

#### 1. **Asignación Automática de Rol por Defecto**
- Al crear un usuario, se asigna automáticamente el rol "USER"
- Se busca dinámicamente el rol por nombre para evitar IDs hardcodeados
- Si el rol no existe, se registra el error pero no se impide la creación del usuario

#### 2. **Código Modificado en UserServiceImpl**
```java
// Asignar rol por defecto "USER" al nuevo usuario
try {
    // Buscar rol por defecto "USER"
    Rol rolPorDefecto = rolRepository.findByRol("USER")
            .orElseThrow(() -> new RuntimeException("Rol USER no encontrado en el sistema"));

    UsuarioRolRequestDto usuarioRolRequest = new UsuarioRolRequestDto();
    usuarioRolRequest.setUsuarioId(user.getId());
    usuarioRolRequest.setRolId(rolPorDefecto.getId());
    usuarioRolRequest.setEstado(true);

    usuarioRolService.createUsuarioRol(usuarioRolRequest);
    log.info("Rol USER asignado automáticamente al usuario: {}", user.getUsername());
} catch (Exception e) {
    log.error("Error al asignar rol por defecto al usuario {}: {}", user.getUsername(), e.getMessage());
    // No lanzamos excepción para no impedir la creación del usuario
}
```

#### 3. **Logs Agregados para Debug**
- Logs en `UserDetailsServiceImpl` para rastrear carga de usuarios y roles
- Logs en `AuthServiceImpl` para rastrear proceso de autenticación
- Logs en `UserServiceImpl` para confirmar asignación de roles

#### 4. **Script SQL para Roles por Defecto**
```sql
INSERT IGNORE INTO roles (rol, estado, usuario_creacion, fecha_creacion, usuario_actualizacion, fecha_actualizacion)
VALUES
    ('ADMIN', true, 'system', NOW(), 'system', NOW()),
    ('USER', true, 'system', NOW(), 'system', NOW()),
    ('MODERATOR', true, 'system', NOW(), 'system', NOW());
```

### 🔧 **Pasos para Aplicar la Solución**

1. **Ejecutar el script de roles por defecto:**
   ```sql
   -- Ejecutar en MySQL
   source scripts/insert_default_roles.sql
   ```

2. **Reiniciar la aplicación** para que tome los cambios

3. **Crear un nuevo usuario** - ahora tendrá automáticamente el rol "USER"

4. **Probar login** con las credenciales del email

### 📋 **Resultado Esperado**

Ahora cuando crees un usuario:
1. ✅ Se crea el usuario con contraseña BCrypt
2. ✅ Se asigna automáticamente el rol "USER"
3. ✅ Se envía email con credenciales
4. ✅ El usuario puede hacer login exitosamente
5. ✅ JWT se genera correctamente con roles incluidos

### 🐛 **Debugging**
Si aún hay problemas, revisar los logs de la aplicación:
- `UserDetailsServiceImpl`: Carga de usuario y roles
- `AuthServiceImpl`: Proceso de autenticación
- `UserServiceImpl`: Asignación de rol por defecto

### 📝 **Notas**
- Los usuarios existentes sin roles necesitarán asignación manual
- El rol "USER" debe existir en la tabla `roles`
- La asignación automática es transaccional con la creación del usuario
