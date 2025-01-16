package kr.hhplus.be.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableScheduling
@SpringBootApplication
public class ServerApplication {

	public final static long THREAD_POOL_SIZE = 100;
	public final static int RESERVATION_HOLD_MINUTES = 5;

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
