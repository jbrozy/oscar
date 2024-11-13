package org.injectables;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    public void test(Integer a, Integer b){
        System.out.printf("Called Placeholder with a (%s) and b (%s)%n", a, b);
    }
}
