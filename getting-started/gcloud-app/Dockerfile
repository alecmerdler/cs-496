FROM jfloff/alpine-python:3.4

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

CMD ["python", "manage.py", "runserver"]
