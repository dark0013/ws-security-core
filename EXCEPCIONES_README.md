# 🚀 SISTEMA DE EXCEPCIONES PERSONALIZADO - GUÍA RÁPIDA

## ✨ Características

- ✅ **Reutilizable**: Mismo patrón para cualquier entidad
- ✅ **Consistente**: 3 constructores en cada excepción
- ✅ **Centralizado**: Códigos y mapeos en un solo lugar
- ✅ **Type-safe**: Constantes en lugar de strings
- ✅ **REST-aware**: HTTP Status automáticos

## 📋 Estructura

```
exception/
├── ApiException.java (Base abstracta)
├── BaseCustomException.java (Para personalizar)
├── ErrorCodeMapper.java (Códigos + Mapeos)
├── GlobalExceptionHandler.java (Manejo global)
│
├── User/
│   ├── UserException.java
│   ├── UserNotFoundException.java
│   └── UserDuplicatedException.java
│
├── Rol/
│   ├── RolNotFoundException.java
│   └── RolDuplicatedException.java
│
├── Product/ (Ejemplo)
│   ├── ProductNotFoundException.java
│   └── ProductDuplicatedException.java
│
└── Template/ (Plantilla para nuevas entidades)
    └── TemplateNotFoundException.java
```

## 🚀 Cómo Usar

### Lanzar Excepción

```java
// Constructor por defecto
throw new UserNotFoundException();

// Con mensaje personalizado
throw new UserNotFoundException("Usuario con email: " + email + " no encontrado");

// Con causa (para debugging)
throw new UserNotFoundException("Error al buscar usuario", databaseException);
```

### Respuesta HTTP Automática

```json
{
    "errorCode": "USER_NOT_FOUND",
    "message": "Usuario no encontrado",
    "status": 404,
    "timestamp": "2026-05-13T12:00:00",
    "path": "/api/v1/users/999"
}
```

## 🔧 Agregar Nueva Excepción

### 📝 Paso 1: Constantes en ErrorCodeMapper

```java
public static class Producto {
    public static final String NOT_FOUND = "PRODUCTO_NOT_FOUND";
    public static final String ALREADY_EXISTS = "PRODUCTO_ALREADY_EXISTS";
}
```

### 📝 Paso 2: Mapeo HTTP

```java
case "PRODUCTO_NOT_FOUND" -> HttpStatus.NOT_FOUND;
case "PRODUCTO_ALREADY_EXISTS" -> HttpStatus.CONFLICT;
```

### 📝 Paso 3: Crear Excepción

```java
public class ProductoNotFoundException extends BaseCustomException {
    public ProductoNotFoundException() {
        super(ErrorCodeMapper.Producto.NOT_FOUND, "Producto no encontrado");
    }
    
    public ProductoNotFoundException(String message) {
        super(ErrorCodeMapper.Producto.NOT_FOUND, message);
    }
    
    public ProductoNotFoundException(String message, Throwable cause) {
        super(ErrorCodeMapper.Producto.NOT_FOUND, message, cause);
    }
}
```

### ✅ ¡Listo! Usar en servicio:

```java
throw new ProductoNotFoundException("Producto con ID " + id + " no existe");
```

## 📊 Mapeo de HTTP Status

| Error Code | Puerto | Significado |
|-----------|--------|------------|
| `USER_NOT_FOUND` | 404 | Usuario no existe |
| `USER_ALREADY_EXISTS` | 409 | Usuario duplicado |
| `ROL_NOT_FOUND` | 404 | Rol no existe |
| `ROL_ALREADY_EXISTS` | 409 | Rol duplicado |
| `VALIDATION_ERROR` | 400 | Datos inválidos |

## 🎯 3 Constructores en Cada Excepción

1. **Sin argumentos**: Mensaje predefinido
   ```java
   new UserNotFoundException()
   ```

2. **Con mensaje**: Personalizar mensaje
   ```java
   new UserNotFoundException("Usuario no encontrado")
   ```

3. **Con causa**: Para trazabilidad
   ```java
   new UserNotFoundException("Error", exception)
   ```

## 🔗 Referencia Rápida

- **Base reutilizable**: `BaseCustomException`
- **Configuración central**: `ErrorCodeMapper.java`
- **Manejador global**: `GlobalExceptionHandler.java`
- **Plantilla**: `exception/Template/TemplateNotFoundException.java`
- **Documentación completa**: `GUIA_COMPLETA_EXCEPCIONES.java`

---

**¡Todo el sistema está listo para usar! Solo sigue los 3 pasos para agregar nuevas excepciones.** 🎉
