package otus;

import otus.service.ServiceTest;
import otus.test.CustomTest;

public class Main {
    public static void main(String[] args) {

        ServiceTest.runTestClass(CustomTest.class);
    }
}