package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.appcontainer.exceptions.IncorrectConfigApplication;
import ru.otus.appcontainer.utils.Reflection;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        List<Class<?>> initialConfigs = Arrays.asList(initialConfigClasses);
        processConfigs(initialConfigs);
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true);
        List<Class<?>> initialConfigs = new ArrayList<>(configClasses);
        processConfigs(initialConfigs);
    }

    private void processConfigs(List<Class<?>> initialConfigs) {
        initialConfigs.sort(Comparator.comparingInt(o -> o.getAnnotation(AppComponentsContainerConfig.class).order()));
        for (Class<?> initialConfig : initialConfigs) {
            processConfig(initialConfig);
        }
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object appConfig = Reflection.instantiate(configClass);
        List<Method> methods = Arrays.stream(Reflection.getMethodsByAnnotation(configClass,AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();

        for (Method method : methods) {
            String name = method.getAnnotation(AppComponent.class).name();
            List<Object> args = getMethodParameters(method);
            Object obj = Reflection.callMethod(appConfig, method, args.toArray());

            if (name != null && obj != null) {
                mapPutWithCheckContain(appComponentsByName, name, obj);
                appComponents.add(obj);
            } else throw new IncorrectConfigApplication("Некорректная конфигурация приложения");
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Object> getMethodParameters(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(this::getAppComponent)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private <K,V> void mapPutWithCheckContain(Map<K,V> map, K key, V value) {
        if (!map.containsKey(key)) {
            map.put(key, value);
        } else throw new IncorrectConfigApplication(String.format("Не должно быть двух Бинов с одинаковым именем: %s", key));
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> objects = appComponents.stream()
                                .filter(o -> componentClass.isAssignableFrom(o.getClass()))
                                .toList();

        if (objects.isEmpty()) {
            throw new IncorrectConfigApplication(String.format("Не найден класс:  %s", componentClass.getName()));
        }
        if (objects.size() > 1) {
            throw new IncorrectConfigApplication("В контексте не должно быть дублирующихся элементов");
        }
        return (C) objects.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object obj = appComponentsByName.get(componentName);
        if (obj == null) {
            throw new IncorrectConfigApplication(String.format("Не найдено имя Бина:  %s", componentName));
        }
        return (C) obj;
    }
}
