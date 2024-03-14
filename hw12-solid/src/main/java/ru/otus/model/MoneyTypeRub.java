package ru.otus.model;

import java.util.function.IntSupplier;

public enum MoneyTypeRub implements IntSupplier {
    V100 (100),
    V500 (500),
    V1000(1000),
    V5000(5000);

    private final int value;
    MoneyTypeRub(int value) {
        this.value = value;
    }

    @Override
    public int getAsInt() {
        return value;
    }
}
