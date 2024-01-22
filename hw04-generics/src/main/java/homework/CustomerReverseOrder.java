package homework;

import java.util.LinkedList;
import java.util.List;

//@SuppressWarnings({"java:S1186", "java:S1135", "java:S1172"}) // при выполнении ДЗ эту аннотацию надо удалить
public class CustomerReverseOrder {

    // todo: 2. надо реализовать методы этого класса
    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private List<Customer> list = new LinkedList<>();

    public void add(Customer customer) {
        if (customer != null){
            list.add(customer);
        }
    }

    public Customer take() {
        if (!list.isEmpty()){
            return list.removeLast();
        } else return null;
    }
}
