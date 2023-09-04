package edu.http_server.server;

import edu.http_server.server.di.Component;
import edu.http_server.server.di.Controller;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApplicationContext {
    private final Map<Class<?>, Object> context = new HashMap<>();
    private static ApplicationContext instance;

    private ApplicationContext() {
    }

    static {
        init();
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    private static void init() {
        Reflections reflection = new Reflections("edu.http_server");
        Set<Class<?>> typesAnnotatedWith = reflection.getTypesAnnotatedWith(Component.class);
        typesAnnotatedWith.stream().filter(aClass -> !aClass.isAnnotation()).forEach(getInstance()::registerBean);
    }

    public List<Object> getBeansAnnotatedWith(Class<? extends Annotation> annotationClass) {
        return context.values().stream().filter(val -> val.getClass().isAnnotationPresent(annotationClass)).toList();
    }

    @SneakyThrows
    private Object registerBean(Class<?> beanClass) {
        Constructor<?> constructor = beanClass.getDeclaredConstructors()[0];
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (context.containsKey(parameterType)) {
                args[i] = context.get(parameterType);
            } else {
                args[i] = registerBean(parameterType);
            }
        }
        Object bean = constructor.newInstance(args);
        context.put(beanClass, bean);
        return bean;
    }

    public <T> T getBean(Class<T> beanClass) {
        return (T) context.get(beanClass);
    }
}
