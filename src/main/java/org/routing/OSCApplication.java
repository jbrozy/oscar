package org.routing;

import org.annotations.OSCMain;
import org.springframework.boot.SpringApplication;

public class OSCApplication {
    public static void run(Class<?> application, String[] args){
        SpringApplication.run(application, args);
        OSCMain entryPoint = application.getAnnotation(OSCMain.class);
        Class<?>[] controllers = entryPoint.controllers();
        // OSCRouter.initialize(controllers);
    }
}
