package com.charliemulic.target.myretail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MyretailApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyretailApplication.class, args);
    }

}

