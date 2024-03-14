package ru.otus.service;

import ru.otus.exceptions.InvalidRequestedMoney;
import ru.otus.model.MoneyBox;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;


public class ATMImpl<T extends Enum<T> & IntSupplier> implements ATM<T> {

    private final Map<T, MoneyBox<T>> moneyboxes;

    public ATMImpl(Set<MoneyBox<T>> moneyboxes) {
        this.moneyboxes = moneyboxes.stream()
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toMap(MoneyBox::getMoneyType, Function.identity(),(a,b)->a, LinkedHashMap::new));
    }

    @Override
    public void putMoney(Map<T, Integer> moneys) {
        moneys.forEach((key, value) -> (Optional
                                        .ofNullable(moneyboxes.get(key))
                                        .orElseThrow())
                                        .incrementCount(value));
    }

    @Override
    public Map<T, Integer> getMoney(long sum) throws InvalidRequestedMoney {

        Map<T, Integer> resultMap = new LinkedHashMap<>();
        long gSum = sum;
        List<T> boxes = moneyboxes.keySet()
                        .stream()
                        .toList();

        // Сначала проверяем возможность выдачи всех средств и собираем всё в коллекцию
        for(T box : boxes){
            int cnt = (int) gSum / box.getAsInt();

            int possibleCount = Math.min(moneyboxes.get(box).getCountBanknotes(), cnt);

            if (possibleCount>0){
                resultMap.put(box, possibleCount);
                gSum -= (long) possibleCount * box.getAsInt();
//                System.out.printf("Возможна выдача: %s количество: %s остаток %s\n",box, possibleCount, gSum);
            }
            if (gSum == 0) { break;};
        }

        // Если выдача возможна, то списываем все денежные средства
        if (gSum == 0 && !resultMap.isEmpty()) {
            resultMap.forEach((k,v)-> moneyboxes.get(k).decrementCount(v));
            return resultMap;
        }else{
            throw new InvalidRequestedMoney("Запрошенные деньги выдать невозможно");
        }
    }

    @Override
    public long getMoneyRest() {
        return moneyboxes.values().stream()
                .mapToLong(MoneyBox::getMoneyRest)
                .sum();
    }

    @Override
    public Map<T, Integer> getAllCountRest() {
        return moneyboxes.entrySet().stream()
               .collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().getCountBanknotes(), (a,b)->a, LinkedHashMap::new));

    }
}


