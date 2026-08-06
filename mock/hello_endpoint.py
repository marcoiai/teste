#!/usr/bin/env python3
import os
import sys
from http.server import BaseHTTPRequestHandler, HTTPServer


def message():
    configured = os.environ.get("HELLO_MESSAGE")
    if configured is not None:
        return configured
    with open("config/hello-message.txt", encoding="utf-8") as file:
        return file.read().strip()


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path != "/hello":
            self.send_error(404)
            return
        body = message().encode()
        self.send_response(200)
        self.send_header("Content-Type", "text/plain; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def log_message(self, *_):
        pass


if "--check" in sys.argv:
    print(message())
else:
    HTTPServer(("0.0.0.0", int(os.environ.get("PORT", "9090"))), Handler).serve_forever()
