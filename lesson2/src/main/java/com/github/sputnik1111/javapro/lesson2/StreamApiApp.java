package com.github.sputnik1111.javapro.lesson2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamApiApp {

    private static final Random random = new Random(100);

    public static void main(String[] args) {

        List<Integer> testList = IntStream.range(0, 30)
                .mapToObj(i -> random.nextInt(15))
                .collect(Collectors.toList());

        System.out.println("Test List: " + testList);

        List<Integer> sortedDescTestList = testList.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        //Вывод для анализа результата
        System.out.println("Sorted Desc test List: " + sortedDescTestList);

        Integer thirdLargestNumber = testList.stream()
                .sorted(Comparator.reverseOrder())
                .skip(2)
                .findFirst()
                .orElse(null);

        System.out.println("Third largest number: " + thirdLargestNumber);

        Integer thirdUniqueLargestNumber = testList.stream()
                .sorted(Comparator.reverseOrder())
                .distinct()
                .skip(2)
                .findFirst()
                .orElse(null);

        System.out.println("Third unique largest number: " + thirdUniqueLargestNumber);

        List<Employee> employees = Employee.generate(10);

        System.out.println("Test employees: " + employees);

        List<String> atLeastThreeNameOlderEngineerDesc = employees.stream()
                .filter(e -> "Инженер".equals(e.getJob()))
                .sorted(Comparator.comparing(Employee::getAge).reversed())
                .limit(3)
                .map(Employee::getName)
                .collect(Collectors.toList());

        System.out.println("At least three name older engineer desc: " + atLeastThreeNameOlderEngineerDesc);

        double avgAge = employees.stream()
                .filter(e -> "Инженер".equals(e.getJob()))
                .collect(Collectors.averagingInt(Employee::getAge));

        long avgAgeInt = Math.round(avgAge);

        System.out.println("Average age of an engineer: " + avgAgeInt);

        List<String> words = List.of(
                "машина",
                "компьютер",
                "компьютер",
                "мышь",
                "клавиатура",
                "ноутбук",
                "ноутбук",
                "ноутбук",
                "стол",
                "кресло",
                "монитор",
                "монитор",
                "монитор",
                "шкаф",
                "пенал"
        );

        String longestWord = words.stream()
                .max(Comparator.comparing(String::length))
                .orElse(null);

        System.out.println("Longest word: " + longestWord);

        String joinedWords = String.join(" ", words);

        Map<String, Long> wordCountMap = Stream.of(joinedWords.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        System.out.println("Word count map: " + wordCountMap);

        List<String> sortedWordsByLengthAndAlpha = words.stream()
                .sorted(Comparator.comparing(String::length).thenComparing(s -> s))
                .collect(Collectors.toList());

        System.out.println("Sorted words by length and alpha: " + sortedWordsByLengthAndAlpha);

        List<String> set5WordsSplitBySpace = IntStream.range(0, 10)
                .mapToObj(i -> generateStrWithWords(words, 5, " "))
                .collect(Collectors.toList());

        String largestWorld = set5WordsSplitBySpace.stream()
                .flatMap(str -> Stream.of(str.split(" ")))
                .max(Comparator.comparing(String::length))
                .orElse(null);

        System.out.println("Largest world: " + largestWorld);

    }

    private static String generateStrWithWords(List<String> words, int countWords, String delimeter) {
        return IntStream.range(0, countWords)
                .mapToObj(i -> words.get(random.nextInt(words.size())))
                .collect(Collectors.joining(delimeter));
    }
}
