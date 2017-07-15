from django.views import View
from django.http import JsonResponse
from models import Boat, Slip
from rest_framework.viewsets import ModelViewSet
from rest_framework.decorators import detail_route, list_route
from rest_framework.response import Response
from rest_framework import status
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

    @detail_route(methods=['post'])
    def set_sail(self, request, pk=None):
        boat = self.get_object()
        boat.at_sea = True
        boat.save()

        for slip in Slip.objects.filter(current_boat=boat):
            slip.current_boat = None
            slip.save()

        return Response(status=status.HTTP_200_OK)


class SlipViewSet(ModelViewSet):
    """
    API endpoint for the slip resource.
    """
    queryset = Slip.objects.all()
    serializer_class = SlipSerializer
