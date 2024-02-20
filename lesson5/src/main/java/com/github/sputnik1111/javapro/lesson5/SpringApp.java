package com.github.sputnik1111.javapro.lesson5;


import com.github.sputnik1111.javapro.lesson5.domain.product.Product;
import com.github.sputnik1111.javapro.lesson5.domain.user.User;
import com.github.sputnik1111.javapro.lesson5.domain.user.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class SpringApp {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                "com.github.sputnik1111.javapro.lesson5"
        );

        UserService userService = applicationContext.getBean(UserService.class);

        userService.clear();

        userService.insert(new User(1L, "Test 1"));

        User user = userService.findById(1L).orElseThrow();

        System.out.println(user);

        userService.addProduct(new Product(
                1L,
                user.getId(),
                "2222222222",
                0,
                Product.TypeProduct.CARD
        ));


        userService.addProduct(new Product(
                2L,
                user.getId(),
                "3333333333333",
                0,
                Product.TypeProduct.BILL
        ));


        Product product = userService.findByProductId(1L).orElseThrow();

        System.out.println(product);

        List<Product> products = userService.findByUserId(user.getId());

        System.out.println(products);


    }
}
