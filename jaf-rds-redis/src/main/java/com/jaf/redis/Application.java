package com.jaf.rds.mysql;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jiahao on 2016/11/14.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan({
        "com.jaf.rds.mysql"})
@MapperScan({
        "com.jaf.rds.mysql"
})
public class Application implements CommandLineRunner {

    public void run(String... args) throws Exception {
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

}
