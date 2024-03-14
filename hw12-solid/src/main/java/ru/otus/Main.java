package ru.otus;

import ru.otus.exceptions.InvalidRequestedMoney;
import ru.otus.model.MoneyBox;
import ru.otus.model.MoneyBoxImpl;
import ru.otus.model.MoneyTypeRub;
import ru.otus.service.ATM;
import ru.otus.service.ATMImpl;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        //  Сделаны необходимые ТЕСТЫ, лежат в тестах!

        Set<MoneyBox<MoneyTypeRub>> moneyBoxes = Arrays.stream(MoneyTypeRub.values())
                                                    .map(t -> new MoneyBoxImpl<>(t,13))
                                                    .collect(Collectors.toSet());

        ATM<MoneyTypeRub> atm = new ATMImpl<>(moneyBoxes);

        System.out.printf("Всего остаток денег: %s\n", atm.getMoneyRest());
        System.out.printf("Всего банкнот: \n %s\n\n", atm.getAllCountRest());

        try {
            int request = 15000;
            System.out.printf("Запрошены деньги: %s\n", request);
            Map<MoneyTypeRub, Integer> result = atm.getMoney(request);
            System.out.printf("Выданы деньги: \n%s\n", result);
        } catch (InvalidRequestedMoney e){
            System.out.println(e.getMessage());
        }

        System.out.printf("Осталось банкнот: \n %s\n", atm.getAllCountRest());
        System.out.printf("Всего остаток денег: %s\n\n", atm.getMoneyRest());

        //////////////////////////////////////////////////////////////////

        try {
            int request = 59900;
            System.out.printf("Запрошены деньги: %s\n", request);
            Map<MoneyTypeRub, Integer> result = atm.getMoney(request);
            System.out.printf("Выданы деньги: \n%s\n", result);
        } catch (InvalidRequestedMoney e){
            System.out.println(e.getMessage());
        }

        System.out.printf("Осталось банкнот: \n %s\n", atm.getAllCountRest());
        System.out.printf("Всего остаток денег: %s\n\n", atm.getMoneyRest());

        //////////////////////////////////////////////////////////////////

        try {
            int request = 50900;
            System.out.printf("Запрошены деньги: %s\n", request);
            Map<MoneyTypeRub, Integer> result = atm.getMoney(request);
            System.out.printf("Выданы деньги: \n%s\n", result);
        } catch (InvalidRequestedMoney e){
            System.out.println(e.getMessage());
        }

        System.out.printf("Осталось банкнот: \n %s\n", atm.getAllCountRest());
        System.out.printf("Всего остаток денег: %s\n\n", atm.getMoneyRest());

        //////////////////////////////////////////////////////////////////

        System.out.println("Кладем деньги: 32 500 \n");

        atm.putMoney(Map.of(MoneyTypeRub.V100, 5,
                            MoneyTypeRub.V500, 4,
                            MoneyTypeRub.V1000, 10,
                            MoneyTypeRub.V5000, 4));

        System.out.printf("Всего банкнот: \n %s\n", atm.getAllCountRest());
        System.out.printf("Всего остаток денег: %s\n", atm.getMoneyRest());
    }
}
