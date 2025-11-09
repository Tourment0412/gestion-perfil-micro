package com.uniquindio.archmicroserv.gestion_perfil.repository;

import com.uniquindio.archmicroserv.gestion_perfil.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, String> {
    Optional<Perfil> findByUsuarioId(String usuarioId);
    boolean existsByUsuarioId(String usuarioId);
}

