package demo.service;

import demo.api.HelloService;

public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        return "Hello World from OSGi!";
    }
}