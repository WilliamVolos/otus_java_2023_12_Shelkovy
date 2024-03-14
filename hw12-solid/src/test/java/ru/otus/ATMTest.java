package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Сервис ATM банкомата должен ")
public class ATMTest {
    private ATM<MoneyTypeRub> atm;

    @BeforeEach
    public void setUp(){
        // Загружаем по 30 купюр в каждую ячейку, всего 198 000 рублей
        Set<MoneyBox<MoneyTypeRub>> moneyBoxes = Arrays.stream(MoneyTypeRub.values())
                                                .map(t -> new MoneyBoxImpl<>(t,30))
                                                .collect(Collectors.toSet());
        atm = new ATMImpl<>(moneyBoxes);
    }

    @DisplayName("принимать банкноты разных номиналов")
    @Test
    void shouldReceiveDifferentTypesBanknotes(){
        // Добавим в каждую ячейку свое произвольное количество и проверим, что они туда попали
        Map<MoneyTypeRub, Integer> moneys = Map.of( MoneyTypeRub.V100, 13,
                                                    MoneyTypeRub.V500, 5,
                                                    MoneyTypeRub.V1000, 9,
                                                    MoneyTypeRub.V5000, 7);

        // Результат содержимого ячеек
        Map<MoneyTypeRub, Integer> results = Map.of( MoneyTypeRub.V100, 43,
                                                     MoneyTypeRub.V500, 35,
                                                     MoneyTypeRub.V1000, 39,
                                                     MoneyTypeRub.V5000, 37);
        atm.putMoney(moneys);

        assertThat(atm.getAllCountRest()).isEqualTo(results);
    }

    @DisplayName("выдавать запрошенную сумму минимальным количеством банкнот")
    @Test
    void shouldIssueSumMinimumBanknotes(){
        // Ожидаемый результат остатка содержимого ячеек в банкомате
        Map<MoneyTypeRub, Integer> resultRest = Map.of(MoneyTypeRub.V100, 26,
                                                       MoneyTypeRub.V500, 30,
                                                       MoneyTypeRub.V1000, 21,
                                                       MoneyTypeRub.V5000, 0);

        // Ожидаемый результат выданных банкнот
        Map<MoneyTypeRub, Integer> resultIssue = Map.of(MoneyTypeRub.V100, 4,
                                                        MoneyTypeRub.V1000, 9,
                                                        MoneyTypeRub.V5000, 30);
        try{
            var verify = atm.getMoney(159400);

            assertThat(verify).isEqualTo(resultIssue);
        } catch (InvalidRequestedMoney e){
            System.out.println(e.getMessage());
        }

        assertThat(atm.getAllCountRest()).isEqualTo(resultRest);
    }

    @DisplayName("выдавать ошибку если сумму нельзя выдать")
    @Test
    void shouldThrowInvalidRequestedMoneyWhenCannotGetSum(){
        assertThrows(InvalidRequestedMoney.class, () -> atm.getMoney(1000000));
        assertThrows(InvalidRequestedMoney.class, () -> atm.getMoney(0));
        assertThrows(InvalidRequestedMoney.class, () -> atm.getMoney(-12530));
    }

    @DisplayName("выдавать сумму остатка денежных средств")
    @Test
    void shouldGetMoneyRest(){
        try{
            atm.getMoney(193700);
        } catch (InvalidRequestedMoney e){
            System.out.println(e.getMessage());
        }
        assertThat(atm.getMoneyRest()).isEqualTo(4300);
    }
}
