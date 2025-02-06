package kr.hhplus.be.server.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Configuration
class TestcontainersConfiguration {

	public static final MySQLContainer<?> MYSQL_CONTAINER;
	public static final GenericContainer<?> REDIS_CONTAINER;

	static {
		// MYSQL 컨테이너 설정
		MYSQL_CONTAINER = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"))
			.withDatabaseName("hhplus")
			.withUsername("test")
			.withPassword("test");
		MYSQL_CONTAINER.start();

		System.setProperty("spring.datasource.url", MYSQL_CONTAINER.getJdbcUrl() + "?characterEncoding=UTF-8&serverTimezone=UTC");
		System.setProperty("spring.datasource.username", MYSQL_CONTAINER.getUsername());
		System.setProperty("spring.datasource.password", MYSQL_CONTAINER.getPassword());

		//Redis 컨테이너 설정
		REDIS_CONTAINER = new GenericContainer<>(DockerImageName.parse("redis:latest"))
				.withExposedPorts(6379)
				.waitingFor(Wait.forListeningPort());
		REDIS_CONTAINER.start();

		System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
		System.setProperty("spring.redis.port", REDIS_CONTAINER.getMappedPort(6379).toString());
	}

	@PreDestroy
	public void preDestroy() {
		if (MYSQL_CONTAINER.isRunning()) {
			MYSQL_CONTAINER.stop();
		}

		if (REDIS_CONTAINER.isRunning()) {
			REDIS_CONTAINER.stop();
		}
	}
}