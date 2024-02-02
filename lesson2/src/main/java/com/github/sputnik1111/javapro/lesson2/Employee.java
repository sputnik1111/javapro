package com.github.sputnik1111.javapro.lesson2;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Employee {
    private final String name;

    private final int age;

    private final String job;

    public Employee(String name, int age, String job) {
        this.name = name;
        this.age = age;
        this.job = job;
    }

    public static List<Employee> generate(int count) {
        if (count < 1) throw new IllegalArgumentException(" count must be greater than 0");
        Random random = new Random(100);

        List<String> jobs = List.of("Инженер", "Программист", "Аналитик", "Тестер");
        List<String> names = List.of("Вася", "Петя", "Маша", "Оля");

        return IntStream.range(0, count)
                .mapToObj(i ->
                        new Employee(
                                names.get(random.nextInt(names.size())) + " " + i,
                                18 + random.nextInt(42),
                                jobs.get(random.nextInt(jobs.size()))
                        )
                ).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getJob() {
        return job;
    }

    @Override
    public String toString() {
        return String.format("Employee(name=%s, age=%d, job=%s)", name, age, job);
    }


}
