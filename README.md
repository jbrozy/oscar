# Oscar

## Description

This library is for educational purposes. The idea is to make dedicated Endpoints for the OSC Protocol by parsing the OSC-Message
and forward it via the OSC Router to the matching Controller. Since this library uses the Reflection-API the processing of a single OSC-Message will be noticably slower.

## Features

- Route OSC messages to specific methods using annotations
- Support for multiple attributes in OSC messages
- Simple and intuitive API for Java developers
- Lightweight and efficient for real-time applications

## Installation

To include `osc-route` in your Java project, you can clone the repository or add it to your build system.

### Using Maven

Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>osc-route</artifactId>
    <version>1.0.0</version> <!-- Update to the latest version -->
</dependency>
```

## Example Controller

```java
package com.osc.controller;

import annotations.xaeto.osc.OSCAttribute;
import annotations.xaeto.osc.OSCNamespace;

public class PositionController {
    @OSCNamespace(name = "/placeholder")
    public void placeholder(@OSCAttribute Integer a, @OSCAttribute Integer b) {
        System.out.println("Called Placeholder with a (%s) and b (%s)".formatted(a, b));
    }
}
```

## Example Main

```java
package com.osc;

import annotations.xaeto.osc.OSCMain;
import controller.xaeto.osc.PositionController;
import exceptions.xaeto.osc.OSCException;
import routing.xaeto.osc.OSCApplication;
import routing.xaeto.osc.OSCRouter;

import java.lang.reflect.InvocationTargetException;

@OSCMain(controllers = {PositionController.class})
public class HelloApplication {
    public static void main(String[] args) throws OSCException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        OSCApplication.run(HelloApplication.class, args);
        OSCRouter.forward("/placeholder", 1, 2);
    }
}
```

This project is licensed under the MIT License. See the LICENSE file for more details.
