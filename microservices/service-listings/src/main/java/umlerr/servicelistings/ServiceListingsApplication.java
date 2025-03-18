package umlerr.servicelistings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "umlerr")
public class ServiceListingsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceListingsApplication.class, args);
    }
}
