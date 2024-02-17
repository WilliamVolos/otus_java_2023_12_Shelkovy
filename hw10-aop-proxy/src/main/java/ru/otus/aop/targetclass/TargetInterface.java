package ru.otus.aop.targetclass;

public interface TargetInterface {
    void calculation(int i);
    void calculation(int i, String str);
    void calculation(int i, String str, Boolean bool);
    String getCalculation(int i, String str, Boolean bool);
}
