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

The response is versioned in `config/hello-message.txt`. Change that file,
commit, and push; Jenkins deploys the mock `/hello` endpoint and verifies the
new committed message before publishing artifacts.

The greeting can be changed at runtime with `-Dhello.message="Your message"`.
