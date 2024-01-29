package com.github.sputnik1111.javapro.lesson1;

import com.github.sputnik1111.javapro.lesson1.annotation.AfterSuite;
import com.github.sputnik1111.javapro.lesson1.annotation.AfterTest;
import com.github.sputnik1111.javapro.lesson1.annotation.BeforeSuite;
import com.github.sputnik1111.javapro.lesson1.annotation.BeforeTest;
import com.github.sputnik1111.javapro.lesson1.annotation.CsvSource;
import com.github.sputnik1111.javapro.lesson1.annotation.Test;
import com.github.sputnik1111.javapro.lesson1.testsuite.TestClass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestRunner {
    public static void main(String[] args)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {

        String basePackage = TestClass.class.getPackageName();

        Set<Class<?>> classesForRunTest = findAllClasses(basePackage);

        for (Class<?> testClass : classesForRunTest) {
            System.out.println("Scan and run test in class - " + testClass.getSimpleName());
            runTests(testClass);
        }

    }

    public static void runTests(Class<?> testClass)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {

        if (testClass == null) return;

        Method beforeSuiteMethod = findMethodWithEmptyParam(testClass, BeforeSuite.class, true)
                .orElse(null);

        Method afterSuiteMethod = findMethodWithEmptyParam(testClass, AfterSuite.class, true)
                .orElse(null);

        if (beforeSuiteMethod != null) beforeSuiteMethod.invoke(null);

        runTestMethods(testClass);

        if (afterSuiteMethod != null) afterSuiteMethod.invoke(null);
    }

    private static void runTestMethods(Class<?> testClass)
            throws InvocationTargetException, IllegalAccessException, InstantiationException {

        Object instanceTestClass = createInstance(testClass);

        List<Method> testMethods = Stream.of(testClass.getDeclaredMethods())
                .filter(TestRunner::hasValidTestAnnotation)
                .peek(m -> m.setAccessible(true))
                .sorted(Comparator.comparingInt(m -> -m.getDeclaredAnnotation(Test.class).priority()))
                .collect(Collectors.toList());

        Method beforeMethod = findMethodWithEmptyParam(testClass, BeforeTest.class, false)
                .orElse(null);

        Method afterSuiteMethod = findMethodWithEmptyParam(testClass, AfterTest.class, false)
                .orElse(null);

        for (Method testMethod : testMethods) {
            if (beforeMethod != null) beforeMethod.invoke(instanceTestClass);
            invokeTestMethod(instanceTestClass, testMethod);
            if (afterSuiteMethod != null) afterSuiteMethod.invoke(instanceTestClass);
        }


    }

    private static boolean hasValidTestAnnotation(Method method) {
        Test testAnnotation = method.getDeclaredAnnotation(Test.class);
        if (testAnnotation == null) return false;
        int priority = testAnnotation.priority();
        if (priority < 1 || priority > 10)
            throw new IllegalArgumentException("priority of Test method " + method.getName() + " should be between 1 and 10");
        return true;
    }

    private static void invokeTestMethod(Object instanceTestClass, Method testMethod)
            throws InvocationTargetException, IllegalAccessException {

        Object[] params = extractMethodParams(
                testMethod,
                testMethod.getAnnotation(CsvSource.class)
        );
        testMethod.invoke(instanceTestClass, params);
    }

    private static Object[] extractMethodParams(Method method, CsvSource csvSource) {
        if (csvSource == null) return new Object[0];
        String[] strParams = csvSource.value().split(",");
        if (strParams.length > method.getParameterCount())
            throw new IllegalArgumentException(method.getName() + " method is marked CsvSource annotation," +
                    " but CsvSource has more params than in method");
        Object[] params = new Object[method.getParameterCount()];
        for (int i = 0; i < strParams.length; i++)
            params[i] = castToType(method.getParameterTypes()[i], strParams[i].trim());
        return params;
    }

    private static Object castToType(Class<?> paramType, String strValue) {
        if (strValue.isBlank() || strValue.contains("null")) return null;
        if (String.class.equals(paramType)) return strValue;
        if (Boolean.class.equals(paramType) || boolean.class.equals(paramType))
            return Boolean.parseBoolean(strValue);
        if (Integer.class.equals(paramType) || int.class.equals(paramType))
            return Integer.valueOf(strValue);
        if (Long.class.equals(paramType) || long.class.equals(paramType))
            return Long.valueOf(strValue);
        if (Float.class.equals(paramType) || float.class.equals(paramType))
            return Float.valueOf(strValue);
        if (Double.class.equals(paramType) || double.class.equals(paramType))
            return Double.valueOf(strValue);
        throw new UnsupportedOperationException("Cant cast type " + paramType + " from str value: " + strValue);
    }

    private static Optional<Method> findMethodWithEmptyParam(
            Class<?> testClass,
            Class<? extends Annotation> annotationClass,
            boolean mustBeStatic
    ) {
        List<Method> methods = Stream.of(testClass.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());

        if (methods.size() > 1)
            throw new IllegalArgumentException("More than one method is marked with " +
                    "the " + annotationClass.getSimpleName() + " annotation in the class " +
                    testClass.getSimpleName());

        if (methods.isEmpty())
            return Optional.empty();

        Method method = methods.get(0);
        if (mustBeStatic && !Modifier.isStatic(method.getModifiers()))
            throw new IllegalArgumentException(method.getName() + " method is not marked as static " +
                    "in the class " + testClass.getSimpleName());

        if (method.getParameterCount() > 0)
            throw new IllegalArgumentException(method.getName() + " method should have 0 params " +
                    "in the class " + testClass.getSimpleName());

        method.setAccessible(true);

        return Optional.of(method);
    }

    private static Object createInstance(Class<?> testClass)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        try {
            Constructor<?> defaultConstructor = testClass.getConstructor();
            return defaultConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("No default constructor found" +
                    " in the class " + testClass.getSimpleName());
        }
    }

    private static Set<Class<?>> findAllClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        if (stream == null)
            throw new IllegalStateException("InputStream for package " + packageName + " is null");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException ignore) {
        }
        return null;
    }
}
