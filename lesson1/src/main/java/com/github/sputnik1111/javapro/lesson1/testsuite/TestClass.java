package com.github.sputnik1111.javapro.lesson1.testsuite;

import com.github.sputnik1111.javapro.lesson1.annotation.AfterSuite;
import com.github.sputnik1111.javapro.lesson1.annotation.AfterTest;
import com.github.sputnik1111.javapro.lesson1.annotation.BeforeSuite;
import com.github.sputnik1111.javapro.lesson1.annotation.BeforeTest;
import com.github.sputnik1111.javapro.lesson1.annotation.CsvSource;
import com.github.sputnik1111.javapro.lesson1.annotation.Test;

public class TestClass {

    @BeforeSuite
    static void beforeAll() {
        System.out.println("Invoke before suite method");
    }

    @AfterSuite
    static void afterAll() {
        System.out.println("Invoke after suite method");
    }

    @BeforeTest
    void beforeEachTest() {
        System.out.println("Invoke before each test method");
    }

    @AfterTest
    void afterEachTest() {
        System.out.println("Invoke after each test method");
    }

    @Test(priority = 1)
    @CsvSource("10, Java, 20, true")
    void test1(Integer number, String str1, Long number2, Boolean flag) {
        System.out.println("Invoke test1 method");
    }

    @Test(priority = 10)
    @CsvSource("10, 20")
    void test2(Integer number, Double number2) {
        System.out.println("Invoke test2 method");
    }

    @Test
    void test3() {
        System.out.println("Invoke test3 method");
    }

    void emptyMethod() {

    }
}
