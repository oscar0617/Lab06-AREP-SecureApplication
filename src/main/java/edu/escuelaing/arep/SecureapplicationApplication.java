package edu.escuelaing.arep;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecureapplicationApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SecureapplicationApplication.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", String.valueOf(getPort())));
        app.run(args);
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }

}
