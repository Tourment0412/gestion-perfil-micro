# Guía de Inicio Rápido - Gestión de Perfil Microservice

Esta guía te permitirá poner en marcha el microservicio de Gestión de Perfil en tu entorno local y ejecutar pruebas básicas.

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL 12 o superior
- Docker y Docker Compose (opcional, para PostgreSQL)

## Instalación Rápida

### 1. Clonar y Compilar

```bash
cd gestion-perfil-micro
./mvnw clean install -DskipTests
```

### 2. Configurar Base de Datos

#### Opción A: PostgreSQL Local

Crear base de datos:
```sql
CREATE DATABASE gestion_perfil;
CREATE USER gestion_perfil WITH PASSWORD 'gestion_perfil_pass';
GRANT ALL PRIVILEGES ON DATABASE gestion_perfil TO gestion_perfil;
```

#### Opción B: PostgreSQL con Docker

```bash
docker run -d --name postgres-gestion-perfil \
  -e POSTGRES_DB=gestion_perfil \
  -e POSTGRES_USER=gestion_perfil \
  -e POSTGRES_PASSWORD=gestion_perfil_pass \
  -p 5432:5432 \
  postgres:15
```

### 3. Configurar Variables de Entorno

Crear archivo `src/main/resources/application-local.properties`:

```properties
# Base de Datos
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/gestion_perfil
SPRING_DATASOURCE_USERNAME=gestion_perfil
SPRING_DATASOURCE_PASSWORD=gestion_perfil_pass

# JPA
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true

# Server
SERVER_PORT=8080
```

### 4. Ejecutar Localmente

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Verificación Inicial

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

Respuesta esperada:
```json
{"status":"UP"}
```

## Pruebas Básicas

### 1. Crear Perfil

```bash
curl -X POST http://localhost:8080/api/v1/perfiles/testuser \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "apodo": "Test User",
    "biografia": "Mi biografía de prueba",
    "informacionContactoPublica": true,
    "organizacion": "Mi Organización",
    "paisResidencia": "Colombia"
  }'
```

Respuesta esperada: HTTP 200 con datos del perfil creado

### 2. Obtener Perfil

```bash
curl -X GET http://localhost:8080/api/v1/perfiles/testuser \
  -H "Authorization: Bearer <token>"
```

Respuesta esperada: HTTP 200 con datos del perfil

### 3. Obtener Perfiles Públicos

```bash
curl -X GET http://localhost:8080/api/v1/perfiles/publicos
```

Respuesta esperada: HTTP 200 con lista de perfiles públicos

### 4. Actualizar Perfil

```bash
curl -X PUT http://localhost:8080/api/v1/perfiles/testuser \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{
    "apodo": "Test User Updated",
    "biografia": "Biografía actualizada"
  }'
```

Respuesta esperada: HTTP 200 con datos actualizados

### 5. Eliminar Perfil

```bash
curl -X DELETE http://localhost:8080/api/v1/perfiles/testuser \
  -H "Authorization: Bearer <token>"
```

Respuesta esperada: HTTP 204 No Content

## Ejecutar Tests

### Tests Unitarios

```bash
./mvnw test
```

### Tests de Integración

```bash
./mvnw verify
```

### Tests con Cobertura

```bash
./mvnw test jacoco:report
```

Ver reporte en: `target/site/jacoco/index.html`

## Verificar Base de Datos

### Conectar a PostgreSQL

```bash
psql -h localhost -U gestion_perfil -d gestion_perfil
```

### Verificar Tabla de Perfiles

```sql
SELECT * FROM perfiles;
```

### Verificar Estructura

```sql
\d perfiles
```

## Troubleshooting

### Error: Connection refused a PostgreSQL

Verificar que PostgreSQL esté corriendo:
```bash
psql -h localhost -U postgres -c "SELECT version();"
```

O verificar contenedor Docker:
```bash
docker ps | grep postgres
```

### Error: Tabla no existe

Verificar que JPA haya creado las tablas. Revisar logs para mensajes de creación de esquema.

Si es necesario, forzar creación:
```properties
SPRING_JPA_HIBERNATE_DDL_AUTO=create
```

**Advertencia**: Esto eliminará todos los datos existentes.

### Error: Puerto 8080 ya en uso

Cambiar el puerto en `application-local.properties`:
```properties
SERVER_PORT=8081
```

## Próximos Pasos

- Revisar `docs/IMPLEMENTATION.md` para detalles de arquitectura
- Configurar integración con RabbitMQ para eventos
- Revisar logs en salida de consola
- Explorar endpoints con herramientas como Postman

