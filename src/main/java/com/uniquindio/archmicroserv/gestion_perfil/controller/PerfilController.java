package com.uniquindio.archmicroserv.gestion_perfil.controller;

import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilRequestDTO;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilResponseDTO;
import com.uniquindio.archmicroserv.gestion_perfil.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/perfiles")
@RequiredArgsConstructor
@Slf4j
public class PerfilController {

    private final PerfilService perfilService;

    @PostMapping("/{usuarioId}")
    public ResponseEntity<PerfilResponseDTO> crearOActualizarPerfil(
            @PathVariable String usuarioId,
            @Valid @RequestBody PerfilRequestDTO requestDTO) {
        log.info("POST /api/v1/perfiles/{}", usuarioId);
        PerfilResponseDTO response = perfilService.crearOActualizarPerfil(usuarioId, requestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<PerfilResponseDTO> obtenerPerfil(@PathVariable String usuarioId) {
        log.info("GET /api/v1/perfiles/{}", usuarioId);
        PerfilResponseDTO response = perfilService.obtenerPerfil(usuarioId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/publicos")
    public ResponseEntity<List<PerfilResponseDTO>> obtenerPerfilesPublicos() {
        log.info("GET /api/v1/perfiles/publicos");
        List<PerfilResponseDTO> response = perfilService.obtenerPerfilesPublicos();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable String usuarioId) {
        log.info("DELETE /api/v1/perfiles/{}", usuarioId);
        perfilService.eliminarPerfil(usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{usuarioId}")
    public ResponseEntity<PerfilResponseDTO> actualizarPerfil(
            @PathVariable String usuarioId,
            @Valid @RequestBody PerfilRequestDTO requestDTO) {
        log.info("PUT /api/v1/perfiles/{}", usuarioId);
        PerfilResponseDTO response = perfilService.crearOActualizarPerfil(usuarioId, requestDTO);
        return ResponseEntity.ok(response);
    }
}

