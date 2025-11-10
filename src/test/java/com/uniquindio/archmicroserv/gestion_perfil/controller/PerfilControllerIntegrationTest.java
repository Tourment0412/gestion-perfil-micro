package com.uniquindio.archmicroserv.gestion_perfil.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilRequestDTO;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilResponseDTO;
import com.uniquindio.archmicroserv.gestion_perfil.service.PerfilService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerfilController.class)
@ActiveProfiles("test")
@DisplayName("Tests de integración para PerfilController")
class PerfilControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PerfilService perfilService;

    @Test
    @DisplayName("POST /api/v1/perfiles/{usuarioId} - Crear perfil exitosamente")
    void testCrearPerfil_Success() throws Exception {
        // Given
        PerfilRequestDTO requestDTO = PerfilRequestDTO.builder()
                .apodo("Test User")
                .biografia("Bio de prueba")
                .informacionContactoPublica(true)
                .paisResidencia("Colombia")
                .build();

        PerfilResponseDTO responseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .apodo("Test User")
                .biografia("Bio de prueba")
                .informacionContactoPublica(true)
                .paisResidencia("Colombia")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/perfiles/{usuarioId}", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("Test User"))
                .andExpect(jsonPath("$.biografia").value("Bio de prueba"))
                .andExpect(jsonPath("$.informacionContactoPublica").value(true));

        verify(perfilService).crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class));
    }

    @Test
    @DisplayName("PUT /api/v1/perfiles/{usuarioId} - Actualizar perfil exitosamente")
    void testActualizarPerfil_Success() throws Exception {
        // Given
        PerfilRequestDTO requestDTO = PerfilRequestDTO.builder()
                .apodo("Updated User")
                .biografia("Updated bio")
                .build();

        PerfilResponseDTO responseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .apodo("Updated User")
                .biografia("Updated bio")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(put("/api/v1/perfiles/{usuarioId}", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("Updated User"));

        verify(perfilService).crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/{usuarioId} - Obtener perfil exitosamente")
    void testObtenerPerfil_Success() throws Exception {
        // Given
        PerfilResponseDTO responseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .apodo("Test User")
                .biografia("Bio de prueba")
                .informacionContactoPublica(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(perfilService.obtenerPerfil("testuser")).thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(get("/api/v1/perfiles/{usuarioId}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("Test User"));

        verify(perfilService).obtenerPerfil("testuser");
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/publicos - Obtener perfiles públicos")
    void testObtenerPerfilesPublicos() throws Exception {
        // Given
        PerfilResponseDTO perfil1 = PerfilResponseDTO.builder()
                .usuarioId("user1")
                .apodo("User 1")
                .informacionContactoPublica(true)
                .createdAt(LocalDateTime.now())
                .build();

        PerfilResponseDTO perfil2 = PerfilResponseDTO.builder()
                .usuarioId("user2")
                .apodo("User 2")
                .informacionContactoPublica(true)
                .createdAt(LocalDateTime.now())
                .build();

        List<PerfilResponseDTO> perfiles = Arrays.asList(perfil1, perfil2);
        when(perfilService.obtenerPerfilesPublicos()).thenReturn(perfiles);

        // When & Then
        mockMvc.perform(get("/api/v1/perfiles/publicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].usuarioId").value("user1"))
                .andExpect(jsonPath("$[1].usuarioId").value("user2"));

        verify(perfilService).obtenerPerfilesPublicos();
    }

    @Test
    @DisplayName("DELETE /api/v1/perfiles/{usuarioId} - Eliminar perfil exitosamente")
    void testEliminarPerfil_Success() throws Exception {
        // Given
        doNothing().when(perfilService).eliminarPerfil("testuser");

        // When & Then
        mockMvc.perform(delete("/api/v1/perfiles/{usuarioId}", "testuser"))
                .andExpect(status().isNoContent());

        verify(perfilService).eliminarPerfil("testuser");
    }

    @Test
    @DisplayName("POST - Crear perfil con datos completos")
    void testCrearPerfilConDatosCompletos() throws Exception {
        // Given
        PerfilRequestDTO requestDTO = PerfilRequestDTO.builder()
                .apodo("Complete User")
                .urlPaginaPersonal("https://example.com")
                .biografia("Complete bio")
                .informacionContactoPublica(true)
                .direccionCorrespondencia("Address 123")
                .organizacion("Company")
                .paisResidencia("USA")
                .linkFacebook("https://facebook.com/user")
                .linkTwitter("https://twitter.com/user")
                .linkLinkedIn("https://linkedin.com/in/user")
                .linkInstagram("https://instagram.com/user")
                .linkGithub("https://github.com/user")
                .linkOtraRed("https://other.com/user")
                .build();

        PerfilResponseDTO responseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .apodo("Complete User")
                .urlPaginaPersonal("https://example.com")
                .biografia("Complete bio")
                .informacionContactoPublica(true)
                .direccionCorrespondencia("Address 123")
                .organizacion("Company")
                .paisResidencia("USA")
                .linkFacebook("https://facebook.com/user")
                .linkTwitter("https://twitter.com/user")
                .linkLinkedIn("https://linkedin.com/in/user")
                .linkInstagram("https://instagram.com/user")
                .linkGithub("https://github.com/user")
                .linkOtraRed("https://other.com/user")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/perfiles/{usuarioId}", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuarioId").value("testuser"))
                .andExpect(jsonPath("$.apodo").value("Complete User"))
                .andExpect(jsonPath("$.linkGithub").value("https://github.com/user"));

        verify(perfilService).crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class));
    }

    @Test
    @DisplayName("GET /api/v1/perfiles/publicos - Retornar lista vacía cuando no hay perfiles públicos")
    void testObtenerPerfilesPublicos_Empty() throws Exception {
        // Given
        when(perfilService.obtenerPerfilesPublicos()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/v1/perfiles/publicos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(perfilService).obtenerPerfilesPublicos();
    }

    @Test
    @DisplayName("POST - Validar content type es JSON")
    void testValidarContentType() throws Exception {
        // Given
        PerfilRequestDTO requestDTO = PerfilRequestDTO.builder()
                .apodo("Test")
                .build();

        PerfilResponseDTO responseDTO = PerfilResponseDTO.builder()
                .usuarioId("testuser")
                .apodo("Test")
                .createdAt(LocalDateTime.now())
                .build();

        when(perfilService.crearOActualizarPerfil(eq("testuser"), any(PerfilRequestDTO.class)))
                .thenReturn(responseDTO);

        // When & Then
        mockMvc.perform(post("/api/v1/perfiles/{usuarioId}", "testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}

