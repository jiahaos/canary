package com.jaf.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class Application implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {

    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
