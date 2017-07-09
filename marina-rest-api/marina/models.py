from django.db import models


class Boat(models.Model):
    id = models.CharField(max_length=30)
    