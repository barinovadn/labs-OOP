package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "application",
        "config",
        "controller",
        "service",
        "repository",
        "entity",
        "dto",
        "search",
        "functions",
        "operations",
        "concurrent",
        "io",
        "exceptions"
})
@EntityScan("entity")
@EnableJpaRepositories("repository")
public class LabsOopApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabsOopApplication.class, args);
    }
}