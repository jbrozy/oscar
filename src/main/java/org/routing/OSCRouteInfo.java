package org.routing;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class OSCRouteInfo {
    private final Method method;
    private Pattern routePattern = null;
    private boolean hasParams;

    public OSCRouteInfo(Method method, String route, boolean hasParams) {
        this.method = method;
        this.hasParams = hasParams;
        if(this.hasParams){
            this.routePattern = Pattern.compile(convertToRegex(route));
        }
    }

    public Method getMethod() {
        return this.method;
    }

    public boolean hasParams(){
        return this.hasParams;
    }

    public Pattern getRoutePattern() {
        return routePattern;
    }

    private static String convertToRegex(String route) {
        return route.replaceAll("\\[int\\]", "(\\\\d+)");
    }
}
