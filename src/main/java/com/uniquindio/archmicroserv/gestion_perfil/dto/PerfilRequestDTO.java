package com.uniquindio.archmicroserv.gestion_perfil.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerfilRequestDTO {
    
    @Size(max = 500, message = "La URL de la página personal no puede exceder 500 caracteres")
    private String urlPaginaPersonal;

    @Size(max = 100, message = "El apodo no puede exceder 100 caracteres")
    private String apodo;

    private Boolean informacionContactoPublica;

    @Size(max = 500, message = "La dirección de correspondencia no puede exceder 500 caracteres")
    private String direccionCorrespondencia;

    @Size(max = 2000, message = "La biografía no puede exceder 2000 caracteres")
    private String biografia;

    @Size(max = 255, message = "La organización no puede exceder 255 caracteres")
    private String organizacion;

    @Size(max = 100, message = "El país de residencia no puede exceder 100 caracteres")
    private String paisResidencia;

    @Size(max = 500, message = "El link de Facebook no puede exceder 500 caracteres")
    private String linkFacebook;

    @Size(max = 500, message = "El link de Twitter no puede exceder 500 caracteres")
    private String linkTwitter;

    @Size(max = 500, message = "El link de LinkedIn no puede exceder 500 caracteres")
    private String linkLinkedIn;

    @Size(max = 500, message = "El link de Instagram no puede exceder 500 caracteres")
    private String linkInstagram;

    @Size(max = 500, message = "El link de GitHub no puede exceder 500 caracteres")
    private String linkGithub;

    @Size(max = 500, message = "El link de otra red no puede exceder 500 caracteres")
    private String linkOtraRed;
}

