from django.views import View
from django.http import JsonResponse
from models import Boat, Slip
from rest_framework.viewsets import ModelViewSet
from serializers import SlipSerializer, BoatSerializer
import time


class MainView(View):

    def get(self, request):
        return JsonResponse({'current_timestamp': time.time()})


class BoatViewSet(ModelViewSet):
    """
    API endpoint for the boat resource.
    """
    queryset = Boat.objects.all()
    serializer_class = BoatSerializer


class SlipViewSet(ModelViewSet):
    """
    API endpoint for the slip resource.
    """
    queryset = Slip.objects.all()
    serializer_class = SlipSerializer
