/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ru.otus;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String... args) {
        List<Integer> example = new ArrayList<>();
        int min=   0;
        int max = 100;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        System.out.println(Lists.transform(example, i -> i*10));
    }
}