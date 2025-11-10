package com.uniquindio.archmicroserv.gestion_perfil;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
})
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
class GestionPerfilApplicationTests {

	@Test
	void contextLoads() {
		// Test that the application context loads successfully
		// Profile "test" is active, so RabbitMQConfig and PerfilEventListener won't load
		// RabbitMQ auto-configuration is also explicitly excluded
	}

}
