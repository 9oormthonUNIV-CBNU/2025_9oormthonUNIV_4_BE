package goormthon_group4.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoormthonBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoormthonBackendApplication.class, args);
    }

}
