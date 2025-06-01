package goormthon_group4.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GoormthonBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoormthonBackendApplication.class, args);
    }

}