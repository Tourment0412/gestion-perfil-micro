package com.uniquindio.archmicroserv.gestion_perfil.service;

import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilRequestDTO;
import com.uniquindio.archmicroserv.gestion_perfil.dto.PerfilResponseDTO;
import com.uniquindio.archmicroserv.gestion_perfil.exceptions.PerfilNotFoundException;
import com.uniquindio.archmicroserv.gestion_perfil.model.Perfil;
import com.uniquindio.archmicroserv.gestion_perfil.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfilService {

    private final PerfilRepository perfilRepository;

    @Transactional
    public PerfilResponseDTO crearOActualizarPerfil(String usuarioId, PerfilRequestDTO requestDTO) {
        log.info("Creando o actualizando perfil para usuario: {}", usuarioId);
        
        Perfil perfil = perfilRepository.findByUsuarioId(usuarioId)
                .orElse(Perfil.builder()
                        .usuarioId(usuarioId)
                        .informacionContactoPublica(false)
                        .build());

        // Actualizar campos
        if (requestDTO.getUrlPaginaPersonal() != null) {
            perfil.setUrlPaginaPersonal(requestDTO.getUrlPaginaPersonal());
        }
        if (requestDTO.getApodo() != null) {
            perfil.setApodo(requestDTO.getApodo());
        }
        if (requestDTO.getInformacionContactoPublica() != null) {
            perfil.setInformacionContactoPublica(requestDTO.getInformacionContactoPublica());
        }
        if (requestDTO.getDireccionCorrespondencia() != null) {
            perfil.setDireccionCorrespondencia(requestDTO.getDireccionCorrespondencia());
        }
        if (requestDTO.getBiografia() != null) {
            perfil.setBiografia(requestDTO.getBiografia());
        }
        if (requestDTO.getOrganizacion() != null) {
            perfil.setOrganizacion(requestDTO.getOrganizacion());
        }
        if (requestDTO.getPaisResidencia() != null) {
            perfil.setPaisResidencia(requestDTO.getPaisResidencia());
        }
        if (requestDTO.getLinkFacebook() != null) {
            perfil.setLinkFacebook(requestDTO.getLinkFacebook());
        }
        if (requestDTO.getLinkTwitter() != null) {
            perfil.setLinkTwitter(requestDTO.getLinkTwitter());
        }
        if (requestDTO.getLinkLinkedIn() != null) {
            perfil.setLinkLinkedIn(requestDTO.getLinkLinkedIn());
        }
        if (requestDTO.getLinkInstagram() != null) {
            perfil.setLinkInstagram(requestDTO.getLinkInstagram());
        }
        if (requestDTO.getLinkGithub() != null) {
            perfil.setLinkGithub(requestDTO.getLinkGithub());
        }
        if (requestDTO.getLinkOtraRed() != null) {
            perfil.setLinkOtraRed(requestDTO.getLinkOtraRed());
        }

        Perfil saved = perfilRepository.save(perfil);
        log.info("Perfil guardado exitosamente para usuario: {}", usuarioId);
        
        return mapToResponseDTO(saved);
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO obtenerPerfil(String usuarioId) {
        log.info("Obteniendo perfil para usuario: {}", usuarioId);
        
        Perfil perfil = perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Perfil no encontrado para usuario: {}", usuarioId);
                    return new PerfilNotFoundException("Perfil no encontrado para el usuario: " + usuarioId);
                });
        
        return mapToResponseDTO(perfil);
    }

    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> obtenerPerfilesPublicos() {
        log.info("Obteniendo perfiles públicos");
        
        List<Perfil> perfiles = perfilRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getInformacionContactoPublica()))
                .collect(Collectors.toList());
        
        return perfiles.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarPerfil(String usuarioId) {
        log.info("Eliminando perfil para usuario: {}", usuarioId);
        
        if (!perfilRepository.existsByUsuarioId(usuarioId)) {
            log.warn("Perfil no encontrado para eliminar: {}", usuarioId);
            throw new PerfilNotFoundException("Perfil no encontrado para el usuario: " + usuarioId);
        }
        
        try {
            perfilRepository.deleteById(usuarioId);
            log.info("Perfil eliminado exitosamente para usuario: {}", usuarioId);
        } catch (Exception e) {
            log.error("Error al eliminar perfil para usuario: {}", usuarioId, e);
            throw new RuntimeException("Error al eliminar perfil: " + e.getMessage(), e);
        }
    }

    private PerfilResponseDTO mapToResponseDTO(Perfil perfil) {
        return PerfilResponseDTO.builder()
                .usuarioId(perfil.getUsuarioId())
                .urlPaginaPersonal(perfil.getUrlPaginaPersonal())
                .apodo(perfil.getApodo())
                .informacionContactoPublica(perfil.getInformacionContactoPublica())
                .direccionCorrespondencia(perfil.getDireccionCorrespondencia())
                .biografia(perfil.getBiografia())
                .organizacion(perfil.getOrganizacion())
                .paisResidencia(perfil.getPaisResidencia())
                .linkFacebook(perfil.getLinkFacebook())
                .linkTwitter(perfil.getLinkTwitter())
                .linkLinkedIn(perfil.getLinkLinkedIn())
                .linkInstagram(perfil.getLinkInstagram())
                .linkGithub(perfil.getLinkGithub())
                .linkOtraRed(perfil.getLinkOtraRed())
                .createdAt(perfil.getCreatedAt())
                .updatedAt(perfil.getUpdatedAt())
                .build();
    }
}

