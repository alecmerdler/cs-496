FROM alpine:latest

RUN apk update && \
    apk add python3 \
            py-pip

WORKDIR /usr/src/app
RUN mkdir -p /usr/src/app
COPY . . 

RUN pip install -r requirements.txt

EXPOSE 80
CMD ["./manage.py", "runserver", "127.0.0.1:80"]
