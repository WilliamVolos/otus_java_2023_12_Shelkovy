package ru.otus.service;

import ru.otus.exceptions.InvalidRequestedMoney;

import java.util.Map;

public interface ATM <T extends Enum<T>> {
    void putMoney(Map<T, Integer> moneys);
    Map<T, Integer> getMoney(long sum) throws InvalidRequestedMoney;
    long getMoneyRest();
    Map<T, Integer> getAllCountRest();
}
