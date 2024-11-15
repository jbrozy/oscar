package org.controller;

import org.annotations.*;

@OSCController(route = "light")
public class LightController {
  @OSCNamespace(route = "test")
  public void test(@OSCAttribute String filter){
    System.out.printf("Light Function called with filter: %s%n", filter);
  }

  @OSCNamespace(route = "x?y?/test", hasParams = true)
  public void parameterized(@OSCAttribute Integer a, @OSCAttribute Integer b, @OSCAttribute String filter){
    // System.out.printf("X: %s Y: %s, message: %s\n", a, b, filter);
  }
}
