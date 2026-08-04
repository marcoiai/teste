package demo.service;

import demo.api.HelloService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class HelloActivator implements BundleActivator {

    private ServiceRegistration<HelloService> registration;

    @Override
    public void start(BundleContext context) throws Exception {

        HelloService service = new HelloServiceImpl();

        registration = context.registerService(
                HelloService.class,
                service,
                null);

        System.out.println("HelloService registered.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {

        if (registration != null) {
            registration.unregister();
        }

        System.out.println("HelloService unregistered.");
    }
}