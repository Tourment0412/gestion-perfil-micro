package com.uniquindio.archmicroserv.gestion_perfil.messaging;

import com.uniquindio.archmicroserv.gestion_perfil.service.PerfilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PerfilEventListener {

    private final PerfilService perfilService;

    @RabbitListener(queues = "gestorperfil.queue")
    @SuppressWarnings("unchecked")
    public void handlePerfilEvent(Map<String, Object> evento) {
        try {
            log.info("📥 Evento recibido en gestorperfil.queue: {}", evento);
            
            String tipoAccion = (String) evento.get("tipoAccion");
            Object payloadObj = evento.get("payload");
            Map<String, Object> payload = payloadObj instanceof Map ? (Map<String, Object>) payloadObj : null;
            
            if (payload == null) {
                log.warn("⚠️ Payload vacío en evento: {}", evento);
                return;
            }
            
            String usuarioId = (String) payload.get("usuario");
            
            if (usuarioId == null || usuarioId.isEmpty()) {
                log.warn("⚠️ Usuario no encontrado en payload: {}", payload);
                return;
            }
            
            log.info("Procesando evento de tipo: {} para usuario: {}", tipoAccion, usuarioId);
            
            // Por ahora, solo logueamos. En el futuro se pueden agregar más acciones
            // como crear perfil automáticamente cuando se registra un usuario
            switch (tipoAccion) {
                case "REGISTRO_USUARIO":
                    log.info("Usuario registrado: {}. Se puede crear perfil por defecto si es necesario.", usuarioId);
                    // Aquí se podría crear un perfil por defecto automáticamente
                    break;
                case "PERFIL_ACTUALIZADO":
                    log.info("Perfil actualizado para usuario: {}", usuarioId);
                    break;
                default:
                    log.debug("Evento de tipo desconocido: {} para usuario: {}", tipoAccion, usuarioId);
                    break;
            }
            
        } catch (Exception e) {
            log.error("❌ Error procesando evento de perfil: {}", evento, e);
            // Re-lanzar para que RabbitMQ pueda manejar el error (DLQ, etc.)
            throw new RuntimeException("Error procesando evento de perfil", e);
        }
    }
}

