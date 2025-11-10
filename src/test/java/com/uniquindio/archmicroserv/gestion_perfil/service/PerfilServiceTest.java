package com.uniquindio.archmicroserv.gestion_perfil.service;

import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilRequestDTO;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilResponseDTO;
import com.uniquindio.archmicroserv.gestion_perfil.exceptions.PerfilNotFoundException;
import com.uniquindio.archmicroserv.gestion_perfil.model.Perfil;
import com.uniquindio.archmicroserv.gestion_perfil.repository.PerfilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitarios para PerfilService")
class PerfilServiceTest {

    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private PerfilService perfilService;

    private Perfil perfil;
    private PerfilRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        perfil = Perfil.builder()
                .usuarioId("testuser")
                .apodo("Test User")
                .biografia("Test bio")
                .informacionContactoPublica(true)
                .paisResidencia("Colombia")
                .linkGithub("https://github.com/testuser")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        requestDTO = PerfilRequestDTO.builder()
                .apodo("Test User")
                .biografia("Test bio")
                .informacionContactoPublica(true)
                .paisResidencia("Colombia")
                .linkGithub("https://github.com/testuser")
                .build();
    }

    @Test
    @DisplayName("Crear perfil nuevo - Camino feliz")
    void testCrearPerfilNuevo_Success() {
        // Given
        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.empty());
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // When
        PerfilResponseDTO result = perfilService.crearOActualizarPerfil("testuser", requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsuarioId());
        assertEquals("Test User", result.getApodo());
        verify(perfilRepository).findByUsuarioId("testuser");
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Actualizar perfil existente - Camino feliz")
    void testActualizarPerfil_Success() {
        // Given
        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // When
        PerfilResponseDTO result = perfilService.crearOActualizarPerfil("testuser", requestDTO);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsuarioId());
        verify(perfilRepository).findByUsuarioId("testuser");
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Obtener perfil - Camino feliz")
    void testObtenerPerfil_Success() {
        // Given
        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.of(perfil));

        // When
        PerfilResponseDTO result = perfilService.obtenerPerfil("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsuarioId());
        assertEquals("Test User", result.getApodo());
        verify(perfilRepository).findByUsuarioId("testuser");
    }

    @Test
    @DisplayName("Obtener perfil - No encontrado")
    void testObtenerPerfil_NotFound() {
        // Given
        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PerfilNotFoundException.class, () -> {
            perfilService.obtenerPerfil("testuser");
        });
        verify(perfilRepository).findByUsuarioId("testuser");
    }

    @Test
    @DisplayName("Obtener perfiles públicos")
    void testObtenerPerfilesPublicos() {
        // Given
        Perfil perfil1 = Perfil.builder()
                .usuarioId("user1")
                .informacionContactoPublica(true)
                .createdAt(LocalDateTime.now())
                .build();
        
        Perfil perfil2 = Perfil.builder()
                .usuarioId("user2")
                .informacionContactoPublica(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        Perfil perfil3 = Perfil.builder()
                .usuarioId("user3")
                .informacionContactoPublica(true)
                .createdAt(LocalDateTime.now())
                .build();

        when(perfilRepository.findAll()).thenReturn(Arrays.asList(perfil1, perfil2, perfil3));

        // When
        List<PerfilResponseDTO> result = perfilService.obtenerPerfilesPublicos();

        // Then
        assertEquals(2, result.size());
        verify(perfilRepository).findAll();
    }

    @Test
    @DisplayName("Eliminar perfil - Camino feliz")
    void testEliminarPerfil_Success() {
        // Given
        when(perfilRepository.existsByUsuarioId("testuser")).thenReturn(true);
        doNothing().when(perfilRepository).deleteById("testuser");

        // When
        perfilService.eliminarPerfil("testuser");

        // Then
        verify(perfilRepository).existsByUsuarioId("testuser");
        verify(perfilRepository).deleteById("testuser");
    }

    @Test
    @DisplayName("Eliminar perfil - No encontrado")
    void testEliminarPerfil_NotFound() {
        // Given
        when(perfilRepository.existsByUsuarioId("testuser")).thenReturn(false);

        // When & Then
        assertThrows(PerfilNotFoundException.class, () -> {
            perfilService.eliminarPerfil("testuser");
        });
        verify(perfilRepository).existsByUsuarioId("testuser");
        verify(perfilRepository, never()).deleteById(anyString());
    }

    @Test
    @DisplayName("Actualizar todos los campos del perfil")
    void testActualizarTodosCampos() {
        // Given
        PerfilRequestDTO fullRequestDTO = PerfilRequestDTO.builder()
                .apodo("New Nickname")
                .urlPaginaPersonal("https://example.com")
                .biografia("New bio")
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

        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.of(perfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // When
        PerfilResponseDTO result = perfilService.crearOActualizarPerfil("testuser", fullRequestDTO);

        // Then
        assertNotNull(result);
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Crear perfil con datos nulos")
    void testCrearPerfilConDatosNulos() {
        // Given
        PerfilRequestDTO emptyDTO = PerfilRequestDTO.builder().build();
        when(perfilRepository.findByUsuarioId("testuser")).thenReturn(Optional.empty());
        when(perfilRepository.save(any(Perfil.class))).thenReturn(perfil);

        // When
        PerfilResponseDTO result = perfilService.crearOActualizarPerfil("testuser", emptyDTO);

        // Then
        assertNotNull(result);
        verify(perfilRepository).save(any(Perfil.class));
    }

    @Test
    @DisplayName("Obtener perfiles públicos cuando no hay ninguno")
    void testObtenerPerfilesPublicos_Empty() {
        // Given
        Perfil perfil1 = Perfil.builder()
                .usuarioId("user1")
                .informacionContactoPublica(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(perfilRepository.findAll()).thenReturn(Arrays.asList(perfil1));

        // When
        List<PerfilResponseDTO> result = perfilService.obtenerPerfilesPublicos();

        // Then
        assertEquals(0, result.size());
    }
}

