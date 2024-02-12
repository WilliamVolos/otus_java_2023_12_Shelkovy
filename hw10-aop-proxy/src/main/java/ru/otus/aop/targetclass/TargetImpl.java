package ru.otus.aop.targetclass;

import ru.otus.aop.annotations.Log;

public class TargetImpl implements TargetInterface{
//    private static final Logger logger = LoggerFactory.getLogger(TargetImpl.class);

    @Override
    public void calculation(int i) {
//        logger.info("Результат: {}",i);
    }

    @Override
    public void calculation(int i, String str) {
//        logger.info("Результат: {}", str+i);
    }

    @Log
    @Override
    public void calculation(int i, String str, Boolean bool) {
//        logger.info("Результат: {}", str+i+bool);
    }

    @Override
    public String getCalculation(int i, String str, Boolean bool) {
        return str+i+bool;
    }

    @Override
    public String toString() {
        return "TargetImpl{}";
    }
}
