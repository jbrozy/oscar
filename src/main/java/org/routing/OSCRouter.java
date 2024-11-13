package org.routing;

import ch.qos.logback.core.joran.sanity.Pair;
import jakarta.annotation.PostConstruct;
import org.annotations.NamespaceNode;
import org.annotations.OSCController;
import org.annotations.OSCNamespace;
import org.exceptions.OSCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class OSCRouter {
    private final static Map<Pattern, Method> routes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> _classCache = new ConcurrentHashMap<>();
    private final ApplicationContext context;

    @Autowired
    public OSCRouter(ApplicationContext context) {
        this.context = context;
    }

    // Method to initialize routes based on annotations
    @PostConstruct
    public void initialize() {
        var beans = context.getBeansWithAnnotation(OSCController.class);
        beans.forEach((name, clazz)-> {
            Class<?> bean = clazz.getClass();
            var controller = bean.getAnnotation(OSCController.class);
            var methods = bean.getMethods();

            for(var method : methods) {
                if(!method.isAnnotationPresent(OSCNamespace.class))
                    continue;
                OSCNamespace namespace = method.getAnnotation(OSCNamespace.class);
                String route = "/" + controller.route() + "/" + namespace.route();
                route = route.replace("?", "(\\d+)");
                System.out.println("Route, " + route);
                routes.putIfAbsent(Pattern.compile(route), method);
            }
        });
    }

    // Return the set of registered routes
    public static Set<Pattern> getRoutes() {
        return routes.keySet();
    }

    // Forward a request to the appropriate method, injecting dependencies as necessary
    public void navigate(String namespace, Object... params)
        throws OSCException, InvocationTargetException, IllegalAccessException {

        final var match = routes
                .entrySet()
                .stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue()))
                .filter(entry -> entry.getKey().matcher(namespace).matches())
                .findFirst();

        if(match.isEmpty()) {
            throw new OSCException("Method not found!");
        }
        var matcher = match.get().getKey().matcher(namespace);
        List<Object> paramsList = new ArrayList<>();
        var result = matcher.toMatchResult();

        System.out.println(matcher.group(1));
        // match.ifPresent(objects -> paramsList.addAll(Arrays.stream(objects.getValue()).toList()));
        // paramsList.add(params);

        // // Validate parameter count and types
        // final Method method = match.get().getKey();
        // final var functionParams = method.getParameterTypes();
        // final int functionParamsCount = functionParams.length;
        // if (functionParamsCount != params.length) {
        //     throw new OSCException("Invalid argument count for calling function: %s".formatted(method.getName()));
        // }

        // for (int i = 0; i < functionParams.length; ++i) {
        //     final Object obj = params[i];
        //     final Class<?> type = functionParams[i];
        //     if (obj != null && !type.isAssignableFrom(obj.getClass())) {
        //         throw new OSCException("Invalid argument at Index: %d".formatted(i));
        //     }
        // }

        // // Use class cache or get a new instance from the ApplicationContext
        // String className = method.getDeclaringClass().getName();
        // final Object instance = _classCache
        //         .computeIfAbsent(className,
        //                 name -> context.getBean(method.getDeclaringClass())
        //         );

        // // Invoke the method with parameters
        // method.invoke(instance, paramsList.toArray());
    }
}

