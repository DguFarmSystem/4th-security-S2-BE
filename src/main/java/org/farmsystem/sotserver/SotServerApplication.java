package org.farmsystem.sotserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SotServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SotServerApplication.class, args);
    }

}
