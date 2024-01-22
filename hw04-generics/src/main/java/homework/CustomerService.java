package homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

//@SuppressWarnings({"java:S1186", "java:S1135", "java:S1172"}) // при выполнении ДЗ эту аннотацию надо удалить
public class CustomerService {
    private TreeMap<Customer, String> map = new TreeMap<>(Comparator.comparingLong(Customer::getScores));;

    // todo: 3. надо реализовать методы этого класса
    // важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны

    public Map.Entry<Customer, String> getSmallest() {
        // Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> entry = map.firstEntry();

        return entry != null ? new AbstractMap.SimpleEntry<>(
                                new Customer(entry.getKey().getId(),
                                             entry.getKey().getName(),
                                             entry.getKey().getScores()
                                ), entry.getValue())
                            : null;
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {

        Map.Entry<Customer, String> entry = map.higherEntry(customer);

        return entry != null ? new AbstractMap.SimpleEntry<>(
                                new Customer(entry.getKey().getId(),
                                        entry.getKey().getName(),
                                        entry.getKey().getScores()
                                ), entry.getValue())
                                : null;
    }

    public void add(Customer customer, String data) {
        if (customer != null) {
            map.put(customer, data);
        }
    }
}
