package ru.otus.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.aop.targetclass.TargetInterface;
import ru.otus.aop.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IoProxy {
    private static final Logger logger = LoggerFactory.getLogger(IoProxy.class);
    private IoProxy(){}

    public static TargetInterface createProxyClass(Class<? extends TargetInterface> objectClass) {
        InvocationHandler handler = new DemoInvocationHandler(Reflection.instantiate(objectClass));
        return  (TargetInterface)
                Proxy.newProxyInstance(IoProxy.class.getClassLoader(), new Class<?>[] {TargetInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TargetInterface myClass;

        // Кэшируем повторные вызовы методов
        private final Map<Method, Boolean> methods = new HashMap<>();

        DemoInvocationHandler(TargetInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Boolean isAnnotation = methods.get(method);

            if (isAnnotation == null){
                isAnnotation = (Arrays.stream(Reflection.getMethodsByAnnotation(myClass.getClass(), Log.class))
                   .filter(m-> (m.getName().equals(method.getName()) && Arrays.equals(m.getParameterTypes(), method.getParameterTypes())))
                   .count() == 1);

                methods.put(method, isAnnotation);
            };

            if (isAnnotation){
                logger.info("executed method: {}, params: {}", method.getName(), Arrays.toString(args));
            }

            return method.invoke(myClass, args);
        }
    }
}
