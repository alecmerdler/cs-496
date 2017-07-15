#!/bin/sh

cd /usr/src/app

# Configure database
python manage.py migrate auth
python manage.py makemigrations
python manage.py migrate --run-syncdb

# Create superuser
echo "from django.contrib.auth.models import User; User.objects.create_superuser('admin', '', 'admin')" | python manage.py shell

# Run server
python manage.py runserver
