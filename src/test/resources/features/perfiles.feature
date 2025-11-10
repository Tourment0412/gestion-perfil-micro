# language: es
# =============================================================================
# ARCHIVO DE CARACTERÍSTICAS (FEATURES) - GESTIÓN DE PERFILES
# =============================================================================

Característica: Gestión de Perfiles de Usuario
  
  Antecedentes:
    Dado que el servicio de gestión de perfiles está disponible

  # ===== CREAR PERFIL =====
  Escenario: Crear un nuevo perfil de usuario
    Dado que tengo un token de autenticación válido
    Cuando creo un perfil con datos válidos
    Entonces la respuesta debe tener estado 200
    Y el cuerpo debe contener los datos del perfil creado

  # ===== CONSULTAR PERFIL =====
  Escenario: Consultar perfil de usuario existente
    Dado que tengo un token de autenticación válido
    Y que existe un perfil creado
    Cuando consulto el perfil del usuario
    Entonces la respuesta debe tener estado 200
    Y el cuerpo debe contener los datos del perfil

  # ===== CONSULTAR PERFILES PÚBLICOS =====
  Escenario: Consultar lista de perfiles públicos
    Cuando consulto la lista de perfiles públicos
    Entonces la respuesta debe tener estado 200
    Y el cuerpo debe contener una lista de perfiles

  # ===== ACTUALIZAR PERFIL =====
  Escenario: Actualizar perfil de usuario existente
    Dado que tengo un token de autenticación válido
    Y que existe un perfil creado
    Cuando actualizo el perfil con nuevos datos
    Entonces la respuesta debe tener estado 200
    Y el cuerpo debe contener los datos actualizados

  # ===== ELIMINAR PERFIL =====
  Escenario: Eliminar perfil de usuario
    Dado que tengo un token de autenticación válido
    Y que existe un perfil creado
    Cuando elimino el perfil del usuario
    Entonces la respuesta debe tener estado 204

  # ===== HEALTH CHECK =====
  Escenario: Verificar salud del servicio de perfiles
    Cuando consulto el endpoint de health check
    Entonces la respuesta debe tener estado 200
    Y el cuerpo debe indicar que el servicio está UP

