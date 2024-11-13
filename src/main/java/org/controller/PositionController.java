package org.controller;

import org.annotations.*;
import org.injectables.*;
import org.springframework.beans.factory.annotation.Autowired;

@OSCController(route = "position")
public class PositionController {
  @Autowired
  final TestService testService;

  public PositionController(TestService testService) {
    this.testService = testService;
  }

  @OSCNamespace(route = "test")
  public void placeholder(@OSCAttribute Integer a, @OSCAttribute Integer b){
    testService.test(a, b);
  }
}
