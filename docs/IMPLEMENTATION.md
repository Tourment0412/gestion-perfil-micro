# Gestión de Perfil Microservice - Documentación de Implementación

## Descripción General

El microservicio de Gestión de Perfil es una aplicación Spring Boot que gestiona la información de perfil de los usuarios del sistema. Proporciona funcionalidades para crear, consultar, actualizar y eliminar perfiles de usuario, incluyendo información personal, redes sociales y preferencias de privacidad.

## Arquitectura

### Componentes Principales

El microservicio sigue una arquitectura en capas:

1. **Controller**: Capa de presentación que maneja las solicitudes HTTP
2. **Service**: Capa de lógica de negocio
3. **Repository**: Capa de acceso a datos (JPA)
4. **Model**: Entidades de dominio
5. **DTO**: Objetos de transferencia de datos
6. **Messaging**: Escucha eventos de RabbitMQ

### Tecnologías Utilizadas

- **Spring Boot 3.x**: Framework principal
- **Spring Data JPA**: Acceso a datos con Hibernate
- **PostgreSQL**: Base de datos relacional
- **Spring AMQP**: Integración con RabbitMQ
- **Lombok**: Reducción de código boilerplate
- **Jakarta Validation**: Validación de datos

## Modelo de Datos

### Entidad Perfil

La entidad `Perfil` representa la información de perfil de un usuario:

- **usuarioId** (String, PK): Identificador único del usuario
- **urlPaginaPersonal** (String): URL de página personal del usuario
- **apodo** (String): Apodo o nombre de usuario público
- **informacionContactoPublica** (Boolean): Indica si la información de contacto es pública
- **direccionCorrespondencia** (String): Dirección postal del usuario
- **biografia** (String, TEXT): Biografía o descripción del usuario
- **organizacion** (String): Organización a la que pertenece el usuario
- **paisResidencia** (String): País de residencia del usuario
- **Links de redes sociales**: Facebook, Twitter, LinkedIn, Instagram, GitHub, otra red
- **createdAt** (LocalDateTime): Fecha de creación del perfil
- **updatedAt** (LocalDateTime): Fecha de última actualización

### Relaciones

- **Relación 1:1 con Usuario**: Cada perfil pertenece a un único usuario identificado por `usuarioId`
- **No hay relación JPA**: La relación es lógica, no física en la base de datos

## Endpoints de la API

### Crear o Actualizar Perfil

- **Endpoint**: `POST /api/v1/perfiles/{usuarioId}`
- **Descripción**: Crea un nuevo perfil o actualiza uno existente
- **Autenticación**: Requerida (JWT Bearer Token)
- **Request Body**:
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
- **Response**: Retorna `PerfilResponseDTO` con todos los datos del perfil

### Obtener Perfiles Públicos

- **Endpoint**: `GET /api/v1/perfiles/publicos`
- **Descripción**: Obtiene todos los perfiles marcados como públicos
- **Autenticación**: No requerida (endpoint público)
- **Response**: Lista de `PerfilResponseDTO` con perfiles públicos

### Actualizar Perfil

- **Endpoint**: `PUT /api/v1/perfiles/{usuarioId}`
- **Descripción**: Actualiza un perfil existente (mismo comportamiento que POST)
- **Autenticación**: Requerida (JWT Bearer Token)
- **Request Body**: Mismo formato que POST

### Eliminar Perfil

- **Endpoint**: `DELETE /api/v1/perfiles/{usuarioId}`
- **Descripción**: Elimina el perfil de un usuario
- **Autenticación**: Requerida (JWT Bearer Token)
- **Response**: 204 No Content

## Componentes de Implementación

### PerfilController

Controlador REST que maneja todas las solicitudes HTTP relacionadas con perfiles.

**Responsabilidades**:
- Validar datos de entrada
- Delegar lógica de negocio al servicio
- Retornar respuestas HTTP apropiadas
- Manejar errores y excepciones

**Métodos principales**:
- `crearOActualizarPerfil()`: Crea o actualiza un perfil
- `obtenerPerfil()`: Obtiene un perfil por usuarioId
- `obtenerPerfilesPublicos()`: Obtiene todos los perfiles públicos
- `actualizarPerfil()`: Actualiza un perfil existente
- `eliminarPerfil()`: Elimina un perfil

### PerfilService

Servicio que contiene la lógica de negocio para la gestión de perfiles.

**Responsabilidades**:
- Validar reglas de negocio
- Coordinar operaciones de base de datos
- Transformar entre entidades y DTOs
- Manejar transacciones

**Métodos principales**:
- `crearOActualizarPerfil()`: Lógica para crear o actualizar perfiles
- `obtenerPerfil()`: Obtiene perfil con manejo de excepciones
- `obtenerPerfilesPublicos()`: Filtra perfiles públicos
- `eliminarPerfil()`: Elimina perfil con validaciones

**Lógica de negocio**:
- Si el perfil no existe, se crea uno nuevo
- Si el perfil existe, se actualiza con los nuevos datos
- Los timestamps se gestionan automáticamente (`@PrePersist`, `@PreUpdate`)
- Validación de datos de entrada con Jakarta Validation

### PerfilRepository

Repositorio JPA para acceso a datos.

**Métodos**:
- `findByUsuarioId()`: Busca perfil por usuarioId
- `findByInformacionContactoPublicaTrue()`: Busca perfiles públicos
- Métodos estándar de JpaRepository (save, delete, etc.)

### DTOs (Data Transfer Objects)

#### PerfilRequestDTO

DTO para recibir datos de entrada en las solicitudes.

**Campos**:
- Todos los campos del perfil excepto `usuarioId`, `createdAt`, `updatedAt`
- Validaciones con anotaciones Jakarta Validation

#### PerfilResponseDTO

DTO para enviar datos en las respuestas.

**Campos**:
- Todos los campos del perfil incluyendo timestamps
- Formato consistente para consumo por clientes

### PerfilEventListener

Componente que escucha eventos de RabbitMQ relacionados con perfiles.

**Funcionalidad**:
- Escucha eventos de eliminación de usuarios
- Sincroniza eliminación de perfiles cuando se elimina un usuario
- Maneja eventos asíncronos del sistema

**Configuración**:
- Exchange: `dominio.events`
- Queue: `perfil.events.queue`
- Routing Key: `auth.deleted`

**Condicional**: Solo se carga si RabbitMQ está configurado y no está en perfil de test

## Flujos de Procesamiento

### Flujo de Creación de Perfil

1. Cliente envía `POST /api/v1/perfiles/{usuarioId}` con datos del perfil
2. `PerfilController.crearOActualizarPerfil()` recibe la solicitud
3. Valida datos de entrada con Jakarta Validation
4. `PerfilService.crearOActualizarPerfil()` se ejecuta:
   - Busca perfil existente por `usuarioId`
   - Si no existe, crea nueva entidad `Perfil`
   - Si existe, actualiza campos con nuevos valores
   - Guarda en base de datos con `@Transactional`
   - Transforma entidad a `PerfilResponseDTO`
5. Retorna respuesta HTTP 200 con datos del perfil

### Flujo de Consulta de Perfil

1. Cliente envía `GET /api/v1/perfiles/{usuarioId}`
2. `PerfilController.obtenerPerfil()` recibe la solicitud
3. `PerfilService.obtenerPerfil()` se ejecuta:
   - Busca perfil en base de datos
   - Si no existe, lanza `PerfilNotFoundException`
   - Transforma entidad a `PerfilResponseDTO`
4. Retorna respuesta HTTP 200 con datos del perfil

### Flujo de Consulta de Perfiles Públicos

1. Cliente envía `GET /api/v1/perfiles/publicos`
2. `PerfilController.obtenerPerfilesPublicos()` recibe la solicitud
3. `PerfilService.obtenerPerfilesPublicos()` se ejecuta:
   - Busca todos los perfiles con `informacionContactoPublica = true`
   - Transforma lista de entidades a lista de DTOs
4. Retorna respuesta HTTP 200 con lista de perfiles públicos

### Flujo de Eliminación de Perfil

1. Cliente envía `DELETE /api/v1/perfiles/{usuarioId}`
2. `PerfilController.eliminarPerfil()` recibe la solicitud
3. `PerfilService.eliminarPerfil()` se ejecuta:
   - Busca perfil en base de datos
   - Si no existe, lanza `PerfilNotFoundException`
   - Elimina perfil de base de datos con `@Transactional`
4. Retorna respuesta HTTP 204 No Content

### Flujo de Eliminación por Evento

1. Domain Service publica evento `ELIMINACION_USUARIO` en RabbitMQ
2. `PerfilEventListener.onUsuarioEliminado()` recibe el evento
3. Extrae `usuarioId` del evento
4. `PerfilService.eliminarPerfil()` elimina el perfil
5. Registra operación en logs

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
SPRING_JPA_PROPERTIES_HIBERNATE_DIALECT=org.hibernate.dialect.PostgreSQLDialect

# RabbitMQ (opcional)
SPRING_RABBITMQ_HOST=rabbitmq
SPRING_RABBITMQ_PORT=5672
SPRING_RABBITMQ_USERNAME=domain_user
SPRING_RABBITMQ_PASSWORD=domain_pass
SPRING_RABBITMQ_VIRTUAL_HOST=foro

# Server
SERVER_PORT=8080
```

### application.properties

Configuración adicional:
- Configuración de logging
- Configuración de Spring Boot Actuator
- Configuración de validación

## Manejo de Transacciones

El servicio utiliza `@Transactional` para garantizar consistencia de datos:

- **Creación/Actualización**: Transacción que garantiza atomicidad
- **Eliminación**: Transacción que garantiza eliminación completa
- **Rollback automático**: En caso de error, se revierten los cambios

## Validación de Datos

### Validaciones Implementadas

- **Campos requeridos**: Validación con `@NotNull`, `@NotBlank`
- **Longitud de campos**: Validación con `@Size`
- **Formato de URLs**: Validación con `@Pattern` o validación personalizada
- **Valores booleanos**: Validación de tipos

### Manejo de Errores

- **PerfilNotFoundException**: Cuando no se encuentra un perfil
- **ValidationException**: Cuando los datos de entrada son inválidos
- **DataIntegrityViolationException**: Cuando hay violaciones de integridad
- **Errores HTTP**: 400 (Bad Request), 404 (Not Found), 500 (Internal Server Error)

## Testing

### Estructura de Tests

- **Unit Tests**: Pruebas de servicios con mocks
- **Integration Tests**: Pruebas de endpoints con `MockMvc`
- **Repository Tests**: Pruebas de acceso a datos

### Cobertura de Tests

El proyecto incluye tests para:
- `PerfilService`: 11 tests unitarios
- `PerfilController`: 9 tests de integración
- Cobertura de casos exitosos y casos de error

## Integración con RabbitMQ

### Configuración Condicional

El microservicio puede funcionar con o sin RabbitMQ:

- **Con RabbitMQ**: Escucha eventos de eliminación de usuarios
- **Sin RabbitMQ**: Funciona normalmente, solo no recibe eventos asíncronos
- **Perfil de test**: Desactiva RabbitMQ para tests

### Eventos Escuchados

- **ELIMINACION_USUARIO**: Elimina perfil cuando se elimina un usuario
- **Formato del evento**: JSON con `usuarioId` y datos adicionales

## Despliegue

### Docker

El microservicio incluye un `Dockerfile` para contenedorización:

```dockerfile
FROM openjdk:21-jdk-slim
# ... configuración del contenedor
```

### Docker Compose

Configurado en `docker-compose.unified.yml`:
- Puerto: 8080
- Dependencias: PostgreSQL, RabbitMQ (opcional)
- Health checks configurados

## Monitoreo y Health Checks

### Spring Boot Actuator

- **Health Endpoint**: `/actuator/health`
- **Info Endpoint**: `/actuator/info`
- **Metrics**: Disponibles en `/actuator/metrics`

### Logging

- **SLF4J + Logback**: Configuración de logging
- **Niveles**: INFO, WARN, ERROR
- **Formato**: JSON estructurado para producción

## Consideraciones de Seguridad

1. **Autenticación JWT**: Validación de tokens en endpoints protegidos
2. **Validación de entrada**: Validación exhaustiva de datos de entrada
3. **Privacidad**: Respeto a la configuración `informacionContactoPublica`
4. **Logging seguro**: No se registran datos sensibles

## Mejoras Futuras

1. **Cache**: Implementar cache para perfiles públicos frecuentemente consultados
2. **Búsqueda**: Implementar búsqueda de perfiles por criterios
3. **Imágenes**: Soporte para imágenes de perfil
4. **Historial**: Mantener historial de cambios en perfiles
5. **Validación de URLs**: Validación más estricta de URLs de redes sociales

