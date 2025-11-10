# Gestión de Perfil Microservice

Microservicio desarrollado en Java con Spring Boot que gestiona la información de perfil de los usuarios del sistema. Proporciona funcionalidades para crear, consultar, actualizar y eliminar perfiles de usuario, incluyendo información personal, redes sociales y preferencias de privacidad.

## Funcionalidades

- Crear o actualizar perfiles de usuario
- Consultar perfiles individuales
- Listar perfiles públicos
- Eliminar perfiles
- Integración con RabbitMQ para eventos de eliminación de usuarios

## Endpoints de la API

### Crear o Actualizar Perfil

- **Endpoint**: `POST /api/v1/perfiles/{usuarioId}`
- **Descripción**: Crea un nuevo perfil o actualiza uno existente
- **Autenticación**: Requerida (JWT Bearer Token)

**Request Body**:
```json
{
  "urlPaginaPersonal": "https://example.com/user",
  "apodo": "Test User",
  "informacionContactoPublica": true,
  "direccionCorrespondencia": "Calle 123",
  "biografia": "Mi biografía personal",
  "organizacion": "Mi Organización",
  "paisResidencia": "Colombia",
  "linkFacebook": "https://facebook.com/user",
  "linkTwitter": "https://twitter.com/user",
  "linkLinkedIn": "https://linkedin.com/in/user",
  "linkInstagram": "https://instagram.com/user",
  "linkGithub": "https://github.com/user",
  "linkOtraRed": "https://example.com/user"
}
```

### Obtener Perfil

- **Endpoint**: `GET /api/v1/perfiles/{usuarioId}`
- **Descripción**: Obtiene el perfil de un usuario específico
- **Autenticación**: Requerida (JWT Bearer Token)

### Obtener Perfiles Públicos

- **Endpoint**: `GET /api/v1/perfiles/publicos`
- **Descripción**: Obtiene todos los perfiles marcados como públicos
- **Autenticación**: No requerida (endpoint público)

### Actualizar Perfil

- **Endpoint**: `PUT /api/v1/perfiles/{usuarioId}`
- **Descripción**: Actualiza un perfil existente (mismo comportamiento que POST)
- **Autenticación**: Requerida (JWT Bearer Token)

### Eliminar Perfil

- **Endpoint**: `DELETE /api/v1/perfiles/{usuarioId}`
- **Descripción**: Elimina el perfil de un usuario
- **Autenticación**: Requerida (JWT Bearer Token)
- **Response**: 204 No Content

## Tecnologías

- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Spring AMQP (RabbitMQ)
- Lombok
- Jakarta Validation

## Modelo de Datos

### Entidad Perfil

- **usuarioId** (String, PK): Identificador único del usuario
- **urlPaginaPersonal** (String): URL de página personal
- **apodo** (String): Apodo o nombre de usuario público
- **informacionContactoPublica** (Boolean): Indica si la información es pública
- **direccionCorrespondencia** (String): Dirección postal
- **biografia** (String, TEXT): Biografía del usuario
- **organizacion** (String): Organización a la que pertenece
- **paisResidencia** (String): País de residencia
- **Links de redes sociales**: Facebook, Twitter, LinkedIn, Instagram, GitHub, otra red
- **createdAt** (LocalDateTime): Fecha de creación
- **updatedAt** (LocalDateTime): Fecha de última actualización

## Configuración

### Variables de Entorno

```properties
# Base de Datos
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-gestion-perfil:5432/gestion_perfil
SPRING_DATASOURCE_USERNAME=gestion_perfil
SPRING_DATASOURCE_PASSWORD=gestion_perfil_pass

# JPA
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=false

# RabbitMQ (opcional)
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=domain_user
SPRING_RABBITMQ_PASSWORD=domain_pass
SPRING_RABBITMQ_VIRTUAL_HOST=foro

# Server
SERVER_PORT=8080
```

## Uso

### Crear Perfil

```bash
curl -X POST http://localhost:8080/api/v1/perfiles/testuser \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "apodo": "Test User",
    "biografia": "Mi biografía",
    "informacionContactoPublica": true
  }'
```

### Obtener Perfil

```bash
curl -X GET http://localhost:8080/api/v1/perfiles/testuser \
  -H "Authorization: Bearer <token>"
```

### Obtener Perfiles Públicos

```bash
curl -X GET http://localhost:8080/api/v1/perfiles/publicos
```

## Estructura del Proyecto

```
gestion-perfil-micro/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/uniquindio/archmicroserv/gestion_perfil/
│   │   │       ├── GestionPerfilApplication.java
│   │   │       ├── controller/
│   │   │       │   └── PerfilController.java
│   │   │       ├── service/
│   │   │       │   └── PerfilService.java
│   │   │       ├── repository/
│   │   │       │   └── PerfilRepository.java
│   │   │       ├── model/
│   │   │       │   └── Perfil.java
│   │   │       ├── dto/
│   │   │       │   ├── PerfilRequestDTO.java
│   │   │       │   └── PerfilResponseDTO.java
│   │   │       ├── messaging/
│   │   │       │   └── PerfilEventListener.java
│   │   │       └── exceptions/
│   │   │           └── PerfilNotFoundException.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── controller/
│       └── service/
├── docs/
│   └── IMPLEMENTATION.md
├── Dockerfile
├── pom.xml
└── README.md
```

## Integración con Docker Compose

El microservicio está configurado en `docker-compose.unified.yml` y se ejecuta en el puerto **8080**.

## Health Check

```bash
curl http://localhost:8080/actuator/health
```

## Testing

El proyecto incluye tests completos:

- **Unit Tests**: PerfilService (11 tests)
- **Integration Tests**: PerfilController (9 tests)

## Integración con RabbitMQ

El microservicio escucha eventos de RabbitMQ:
- **Queue**: `gestorperfil.queue`
- **Eventos**: `REGISTRO_USUARIO`, `PERFIL_ACTUALIZADO`
- **Condicional**: Solo se carga si RabbitMQ está configurado y no está en perfil de test

## Manejo de Transacciones

El servicio utiliza `@Transactional` para garantizar consistencia de datos:
- Creación/Actualización: Transacción que garantiza atomicidad
- Eliminación: Transacción que garantiza eliminación completa
- Rollback automático: En caso de error, se revierten los cambios

## Validación de Datos

- Campos requeridos: Validación con `@NotNull`, `@NotBlank`
- Longitud de campos: Validación con `@Size`
- Formato de URLs: Validación con `@Pattern` o validación personalizada

## Notas

- Para documentación detallada, consultar `docs/IMPLEMENTATION.md`
- El microservicio puede funcionar con o sin RabbitMQ
- Los timestamps se gestionan automáticamente con `@PrePersist` y `@PreUpdate`

