package org.oscar;

import org.annotations.OSCMain;
import org.controller.*;

import org.routing.OSCApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@OSCMain(controllers = { PositionController.class, LightController.class })
@ComponentScan(basePackages = {"org.routing", "org.controller", "org.injectables"})
public class Main {
    public static void main(String[] args) {
        OSCApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            String[] beanNames = ctx.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                System.out.println("Registered: " + beanName);
            }
        };
    }
}
