package kr.hhplus.be.server.test.base;

import kr.hhplus.be.server.test.util.ClearDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseIntegretionTest {

	@Autowired
	ClearDatabase clearDatabase;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@Transactional
	@BeforeEach
	void baseSetUp() {
		clearDatabase.clear();
		redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
	}

	protected void setUp() {
		// do nothing
	}
}
