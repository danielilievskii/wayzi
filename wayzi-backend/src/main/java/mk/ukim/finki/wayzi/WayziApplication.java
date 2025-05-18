package mk.ukim.finki.wayzi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class WayziApplication {

	public static void main(String[] args) {
		SpringApplication.run(WayziApplication.class, args);
	}

}
