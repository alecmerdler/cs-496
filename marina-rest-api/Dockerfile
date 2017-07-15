FROM alpine:latest

RUN apk update && \
    apk add python3 \
            py-pip

WORKDIR /usr/src/app
RUN mkdir -p /usr/src/app
COPY . .

RUN pip install -r requirements.txt

# Configure database
RUN python manage.py migrate auth && \
    python manage.py makemigrations && \
    python manage.py migrate
# Create superuser
RUN echo "from django.contrib.auth.models import User; User.objects.create_superuser('admin', '', 'admin')" | python manage.py shell

EXPOSE 8000
CMD ["python", "manage.py", "runserver", "127.0.0.1:8000"]
