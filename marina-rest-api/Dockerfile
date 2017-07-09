FROM alpine:latest

RUN apk update && \
    apk add python3 \
            py-pip

WORKDIR /usr/src/app
RUN mkdir -p /usr/src/app
COPY . . 

RUN pip install -r requirements.txt

EXPOSE 8000
CMD ["./manage.py", "runserver"]
