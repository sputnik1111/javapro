package com.github.sputnik1111.javapro.lesson4;

import com.github.sputnik1111.javapro.lesson4.dao.User;
import com.github.sputnik1111.javapro.lesson4.dao.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SpringContextExampleApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                "com.github.sputnik1111.javapro.lesson4"
        );

        UserService userService = applicationContext.getBean(UserService.class);

        userService.clear();

        userService.insert(new User(1L, "Test 1"));

        User user = userService.findById(1L).orElseThrow();

        System.out.println(user);

        userService.update(1L, "Test 1 change name");

        user = userService.findById(1L).orElseThrow();

        System.out.println(user);

        userService.insert(new User(2L, "Test 2"));

        List<User> users = userService.findAll();

        System.out.println(users);

        users.forEach(u -> userService.delete(u.getId()));

        users = userService.findAll();

        System.out.println(users);

    }
}
