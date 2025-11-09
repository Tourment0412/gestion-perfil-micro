package com.uniquindio.archmicroserv.gestion_perfil.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilRequestDTO;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilResponseDTO;
import com.uniquindio.archmicroserv.gestion_perfil.exceptions.PerfilNotFoundException;
import com.uniquindio.archmicroserv.gestion_perfil.service.PerfilService;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios para PerfilController - Camino Feliz")
class PerfilControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PerfilService perfilService;

    @InjectMocks
    private PerfilController perfilController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private PerfilRequestDTO perfilRequestDTO;
    private PerfilResponseDTO perfilResponseDTO;

    @BeforeEach
    @SuppressWarnings("unused") // JUnit automatically invokes @BeforeEach methods
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(perfilController)
                .setControllerAdvice(new com.uniquindio.archmicroserv.gestion_perfil.exceptions.GlobalExceptionHandler())
                .build();

        perfilRequestDTO = new PerfilRequestDTO(
                "https://testuser.example.com",
                "TestUser",
                true,
                "Calle 123",
                "Biografía de prueba",
                "Uniquindio",
                "Colombia",
                "https://facebook.com/test",
                "https://twitter.com/test",
                "https://linkedin.com/test",
                "https://instagram.com/test",
                "https://github.com/test",
                "https://otra.com/test"
        );

        perfilResponseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .urlPaginaPersonal("https://testuser.example.com")
                .apodo("TestUser")
                .informacionContactoPublica(true)
                .direccionCorrespondencia("Calle 123")
                .biografia("Biografía de prueba")
                .organizacion("Uniquindio")
                .paisResidencia("Colombia")
                .linkFacebook("https://facebook.com/test")
                .linkTwitter("https://twitter.com/test")
                .linkLinkedIn("https://linkedin.com/test")
                .linkInstagram("https://instagram.com/test")
                .linkGithub("https://github.com/test")
                .linkOtraRed("https://otra.com/test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/perfiles/{usuarioId} - Crear perfil exitosamente")
    void testCrearPerfilExitoso() throws Exception {
        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(perfilResponseDTO);

        mockMvc.perform(post("/api/v1/perfiles/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("TestUser"))
                .andExpect(jsonPath("$.informacionContactoPublica").value(true));

        verify(perfilService, times(1)).crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/{usuarioId} - Obtener perfil exitosamente")
    void testObtenerPerfilExitoso() throws Exception {
        when(perfilService.obtenerPerfil("testuser")).thenReturn(perfilResponseDTO);

        mockMvc.perform(get("/api/v1/perfiles/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("TestUser"));

        verify(perfilService, times(1)).obtenerPerfil("testuser");
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/publicos - Listar perfiles públicos exitosamente")
    void testObtenerPerfilesPublicosExitoso() throws Exception {
        List<PerfilResponseDTO> perfiles = Arrays.asList(perfilResponseDTO);
        when(perfilService.obtenerPerfilesPublicos()).thenReturn(perfiles);

        mockMvc.perform(get("/api/v1/perfiles/publicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].usuarioId").value("testuser"))
                .andExpect(jsonPath("$[0].informacionContactoPublica").value(true));

        verify(perfilService, times(1)).obtenerPerfilesPublicos();
    }

    @Test
    @DisplayName("PUT /api/v1/perfiles/{usuarioId} - Actualizar perfil exitosamente")
    void testActualizarPerfilExitoso() throws Exception {
        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(perfilResponseDTO);

        mockMvc.perform(put("/api/v1/perfiles/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(perfilRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value("testuser"));

        verify(perfilService, times(1)).crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/perfiles/{usuarioId} - Eliminar perfil exitosamente")
    void testEliminarPerfilExitoso() throws Exception {
        doNothing().when(perfilService).eliminarPerfil("testuser");

        mockMvc.perform(delete("/api/v1/perfiles/testuser"))
                .andExpect(status().isNoContent());

        verify(perfilService, times(1)).eliminarPerfil("testuser");
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/{usuarioId} - Perfil no encontrado")
    void testObtenerPerfilNoEncontrado() throws Exception {
        when(perfilService.obtenerPerfil("noexiste"))
                .thenThrow(new PerfilNotFoundException("Perfil no encontrado para el usuario: noexiste"));

        mockMvc.perform(get("/api/v1/perfiles/noexiste"))
                .andExpect(status().isNotFound());

        verify(perfilService, times(1)).obtenerPerfil("noexiste");
    }
}

