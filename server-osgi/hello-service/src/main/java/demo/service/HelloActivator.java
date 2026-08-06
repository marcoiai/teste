package demo.service;

import demo.api.HelloService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;


public class HelloActivator implements BundleActivator {

    private ServiceRegistration<HelloService> registration;
    private HttpServer httpServer;

    @Override
    public void start(BundleContext context) throws Exception {

        HelloService service = new HelloServiceImpl();

        registration = context.registerService(
                HelloService.class,
                service,
                null);

        System.out.println("HelloService registered.");
        httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
        httpServer.createContext("/hello", exchange -> {
            byte[] body = service.sayHello().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, body.length);
            try (var output = exchange.getResponseBody()) {
                output.write(body);
            }
        });
        httpServer.start();
    }

    @Override
    public void stop(BundleContext context) throws Exception {

        if (registration != null) {
            registration.unregister();
        }
        if (httpServer != null) {
            httpServer.stop(0);
        }

        System.out.println("HelloService unregistered.");
    }
}
