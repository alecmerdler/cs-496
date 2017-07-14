from django.db import models


class Boat(models.Model):
    id = models.CharField(max_length=30)
    name = models.CharField(max_length=30)
    type = models.CharField(max_length=30)
    length = models.PositiveIntegerField()
    at_sea = models.BooleanField()


class Slip(models.Model):
    id = models.CharField(max_length=30)
    number = models.PositiveIntegerField()
    current_boat = models.ForeignKey(Boat)
    arrival_date = models.CharField(max_length=9)
