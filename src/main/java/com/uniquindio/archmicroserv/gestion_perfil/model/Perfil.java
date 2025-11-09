package com.uniquindio.archmicroserv.gestion_perfil.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "perfiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Perfil {

    @Id
    @Column(name = "usuario_id", length = 255)
    private String usuarioId;

    @Column(name = "url_pagina_personal", length = 500)
    private String urlPaginaPersonal;

    @Column(name = "apodo", length = 100)
    private String apodo;

    @Column(name = "informacion_contacto_publica", nullable = false)
    @Builder.Default
    private Boolean informacionContactoPublica = false;

    @Column(name = "direccion_correspondencia", length = 500)
    private String direccionCorrespondencia;

    @Column(name = "biografia", columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "organizacion", length = 255)
    private String organizacion;

    @Column(name = "pais_residencia", length = 100)
    private String paisResidencia;

    @Column(name = "link_facebook", length = 500)
    private String linkFacebook;

    @Column(name = "link_twitter", length = 500)
    private String linkTwitter;

    @Column(name = "link_linkedin", length = 500)
    private String linkLinkedIn;

    @Column(name = "link_instagram", length = 500)
    private String linkInstagram;

    @Column(name = "link_github", length = 500)
    private String linkGithub;

    @Column(name = "link_otra_red", length = 500)
    private String linkOtraRed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

