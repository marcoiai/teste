#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

#include <cstring>
#include <iostream>
#include <string>

int main(int argc, char** argv) {
    if (argc == 2 && std::string(argv[1]) == "--help") {
        std::cout << "Usage: hello-client [host] [port]\n";
        return 0;
    }

    const char* host = argc > 1 ? argv[1] : "127.0.0.1";
    const int port = argc > 2 ? std::stoi(argv[2]) : 9090;

    int socket_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (socket_fd < 0) {
        std::cerr << "Could not create client socket\n";
        return 1;
    }

    sockaddr_in address{};
    address.sin_family = AF_INET;
    address.sin_port = htons(port);
    if (inet_pton(AF_INET, host, &address.sin_addr) != 1 ||
        connect(socket_fd, reinterpret_cast<sockaddr*>(&address), sizeof(address)) < 0) {
        std::cerr << "Could not connect to OSGi service at " << host << ':' << port << '\n';
        close(socket_fd);
        return 1;
    }

    const char request[] = "HELLO\n";
    send(socket_fd, request, sizeof(request) - 1, 0);

    char response[256]{};
    ssize_t bytes = recv(socket_fd, response, sizeof(response) - 1, 0);
    close(socket_fd);
    if (bytes <= 0) {
        std::cerr << "OSGi service returned no response\n";
        return 1;
    }

    std::cout << std::string(response, static_cast<std::size_t>(bytes));
    return 0;
}
