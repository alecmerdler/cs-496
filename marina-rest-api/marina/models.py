from django.db import models
import uuid


class Boat(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    name = models.CharField(max_length=30)
    type = models.CharField(max_length=30)
    length = models.PositiveIntegerField()
    at_sea = models.BooleanField(default=True)

    class Meta:
        app_label = 'marina'


class Slip(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    number = models.PositiveIntegerField()
    current_boat = models.ForeignKey(Boat, null=True)
    arrival_date = models.CharField(max_length=9)

    class Meta:
        app_label = 'marina'
