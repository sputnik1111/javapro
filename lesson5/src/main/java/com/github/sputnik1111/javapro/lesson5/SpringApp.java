package com.github.sputnik1111.javapro.lesson5;


import com.github.sputnik1111.javapro.lesson5.domain.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringApp {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SpringApp.class, args);

        UserService userService = applicationContext.getBean(UserService.class);

        userService.clear();

    }
}
