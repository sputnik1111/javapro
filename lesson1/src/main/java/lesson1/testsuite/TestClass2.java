package lesson1.testsuite;

import lesson1.annotation.AfterSuite;
import lesson1.annotation.AfterTest;
import lesson1.annotation.BeforeSuite;
import lesson1.annotation.BeforeTest;
import lesson1.annotation.CsvSource;
import lesson1.annotation.Test;

public class TestClass2 {

    @BeforeSuite
    static void beforeAll(){
        System.out.println("Invoke before suite method ");
    }

    @AfterSuite
    static void afterAll(){
        System.out.println("Invoke after suite method");
    }

    @BeforeTest
    void beforeEachTest(){
        System.out.println("Invoke before each test method");
    }

    @AfterTest
    void afterEachTest(){
        System.out.println("Invoke after each test method");
    }

    @Test(priority = 4)
    @CsvSource("10, Java, 20, true")
    void test1(Integer number,String str1,Long number2,Boolean flag){
        System.out.println("Invoke test1 method");
    }

    @Test(priority = 3)
    @CsvSource("10, 20")
    void test2(Integer number,Double number2){
        System.out.println("Invoke test2 method");
    }

}
