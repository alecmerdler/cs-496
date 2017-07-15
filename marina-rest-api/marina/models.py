from django.db import models


class Boat(models.Model):
    id = models.CharField(max_length=30)
    name = models.CharField(max_length=30)
    type = models.CharField(max_length=30)
    length = models.PositiveIntegerField()
    at_sea = models.BooleanField(default=False)

    class Meta:
        app_label = 'marina'


class Slip(models.Model):
    id = models.CharField(max_length=30)
    number = models.PositiveIntegerField()
    current_boat = models.ForeignKey(Boat, null=True)
    arrival_date = models.CharField(max_length=9)

    class Meta:
        app_label = 'marina'
