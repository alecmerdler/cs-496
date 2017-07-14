from django.views import View
from django.http import JsonResponse
from models import Boat, Slip
import time


class MainView(View):

    def get(self, request):
        return JsonResponse({'current_timestamp': time.time()})


class BoatView(View):
    """
    API endpoint for the boat resource.
    """
    def get(self, request):
        boats = Boat.objects.all()

        return JsonResponse([{'id': boat.id} for boat in boats])


class SlipView(View):
    """
    API endpoint for the slip resource.
    """
