#!/usr/bin/env python3
import os
from http.server import BaseHTTPRequestHandler, HTTPServer


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path != "/hello":
            self.send_error(404)
            return
        body = os.environ.get("HELLO_MESSAGE", "Hello World from OSGi!").encode()
        self.send_response(200)
        self.send_header("Content-Type", "text/plain; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        self.wfile.write(body)

    def log_message(self, *_):
        pass


HTTPServer(("0.0.0.0", int(os.environ.get("PORT", "9090"))), Handler).serve_forever()
