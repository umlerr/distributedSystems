package umlerr.servicecars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "umlerr")
public class ServiceCarsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCarsApplication.class, args);
    }
}
