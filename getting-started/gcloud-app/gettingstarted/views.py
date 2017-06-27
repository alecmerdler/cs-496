from django.views import View
from django.http import JsonResponse
import time


class MainView(View):

    def get(self, request):
        return JsonResponse({'current_timestamp': time.time()})
