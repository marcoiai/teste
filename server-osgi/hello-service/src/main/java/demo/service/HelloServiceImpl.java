package demo.service;

import demo.api.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return System.getProperty("hello.message", "Hello World from OSGi!");
    }
}
