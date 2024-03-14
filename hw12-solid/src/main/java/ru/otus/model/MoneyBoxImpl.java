package ru.otus.model;

import java.util.function.IntSupplier;

public class MoneyBoxImpl<T extends Enum<T> & IntSupplier> implements MoneyBox<T>, Comparable<MoneyBox<T>>{

    private final T vType;
    private int count;

    public MoneyBoxImpl(T vType, int count) {
        this.vType = vType;
        this.count = count;
    }

    @Override
    public T getMoneyType() {
        return vType;
    }

    @Override
    public void incrementCount(int count) {
        this.count += count;
    }

    @Override
    public boolean decrementCount(int count) {
       if(this.count < count) {
           return false;
       }
       this.count -= count;
       return true;
    }

    @Override
    public long getMoneyRest() {
        return (long) vType.getAsInt() * count;
    }

    @Override
    public int getCountBanknotes() {
        return count;
    }

    @Override
    public int compareTo(MoneyBox<T> o) {
        return Integer.compare(this.getMoneyType().getAsInt(), o.getMoneyType().getAsInt());
    }
}
