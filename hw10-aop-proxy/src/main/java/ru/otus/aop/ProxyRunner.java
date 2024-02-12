package ru.otus.aop;

import ru.otus.aop.proxy.IoProxy;
import ru.otus.aop.targetclass.TargetImpl;
import ru.otus.aop.targetclass.TargetInterface;

public class ProxyRunner {

    public static void main(String[] args) {
        TargetInterface myClass = IoProxy.createProxyClass(TargetImpl.class);
        myClass.calculation(7);
        myClass.calculation(7, "Привет");
        myClass.calculation(7, "Привет", true);

        myClass.calculation(15, "Повторный вызов");
        myClass.calculation(10, "Повторный вызов", false);
        myClass.calculation(16);
    }
}
