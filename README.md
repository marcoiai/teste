Minimal C++/OSGi integration demo for Jenkins CI.

Current state:
- `native-hello` is a C++17 client with a CTest smoke test
- `hello-api` defines the OSGi service contract
- `hello-service` registers `HelloService` as the OSGi compile fixture
- the C++ client is the native compile/test fixture

Build all bundles with:

```sh
cmake -S native-hello -B native-hello/build
cmake --build native-hello/build
ctest --test-dir native-hello/build --output-on-failure
mvn -B -ntp -f server-osgi/pom.xml clean package
```

Jenkins archives the generated C++ executable and OSGi bundles as build
artifacts for downstream deployment.

The Jenkins pipeline also deploys a temporary mock endpoint at `/hello` and
verifies the configurable `HELLO_MESSAGE` value before publishing artifacts.

The greeting can be changed at runtime with `-Dhello.message="Your message"`.
