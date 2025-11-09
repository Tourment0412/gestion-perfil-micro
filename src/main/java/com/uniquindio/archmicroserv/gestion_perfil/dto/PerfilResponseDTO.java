package com.uniquindio.archmicroserv.gestion_perfil.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PerfilResponseDTO {
    
    private String usuarioId;
    private String urlPaginaPersonal;
    private String apodo;
    private Boolean informacionContactoPublica;
    private String direccionCorrespondencia;
    private String biografia;
    private String organizacion;
    private String paisResidencia;
    private String linkFacebook;
    private String linkTwitter;
    private String linkLinkedIn;
    private String linkInstagram;
    private String linkGithub;
    private String linkOtraRed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

