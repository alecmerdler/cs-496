from rest_framework import serializers
from models import Boat, Slip


class BoatSerializer(serializers.ModelSerializer):
    class Meta:
        model = Boat
        fields = ('id', 'name', 'type', 'length', 'at_sea')


class SlipSerializer(serializers.ModelSerializer):
    class Meta:
        model = Slip
        fields = ('id', 'number', 'current_boat', 'arrival_date')
