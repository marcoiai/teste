cmake_minimum_required(VERSION 3.16)
project(hello_client LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_executable(hello-client src/hello_client.cpp)

enable_testing()
add_test(NAME hello_client_help COMMAND hello-client --help)
