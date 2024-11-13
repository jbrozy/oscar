package org.annotations;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Controller
@Retention(RetentionPolicy.RUNTIME)
public @interface OSCController {
    String route();
}
