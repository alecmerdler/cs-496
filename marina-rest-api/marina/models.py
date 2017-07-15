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

    def __str__(self):
        return u"%s" % (self.name)


class Slip(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    number = models.PositiveIntegerField()
    current_boat = models.ForeignKey(Boat, null=True, on_delete=models.SET_NULL)
    arrival_date = models.CharField(max_length=9)

    def delete(self):
        if self.current_boat:
            self.current_boat.at_sea = True
            self.current_boat.save()
        super(Slip, self).delete()

    class Meta:
        app_label = 'marina'

    def __str__(self):
        return u"%s" % (self.number)
