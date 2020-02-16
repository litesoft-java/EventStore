package org.litesoft.events;

import org.litesoft.restish.support.auth.Authorization;
import org.litesoft.restish.support.auth.ThreadLocalAuthorization;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public Authorization createAuthorization() {
        return new ThreadLocalAuthorization();
    }


}