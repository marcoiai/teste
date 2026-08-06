FROM python:3.12-alpine

WORKDIR /app
COPY mock/hello_endpoint.py .
COPY config/hello-message.txt config/hello-message.txt

ENV PORT=9090
EXPOSE 9090

CMD ["python3", "hello_endpoint.py"]
