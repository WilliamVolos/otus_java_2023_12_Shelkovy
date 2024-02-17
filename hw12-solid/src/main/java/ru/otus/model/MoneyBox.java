package ru.otus.model;

public interface MoneyBox<T extends Enum<T>>{
    T getMoneyType();
    void incrementCount(int count);
    boolean decrementCount(int count);
    long getMoneyRest();
    int getCountBanknotes();
}
